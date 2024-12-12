package Pucknotes.Server.Account;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import Pucknotes.Server.Response.Types.ResourceConflictException;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Response.Types.UnauthorizedException;

class AccountServiceTest {

  @Mock
  private AccountRepository repository;

  @Mock
  private PasswordEncoder encoder;

  @InjectMocks
  private AccountService accountService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void registerAccount_ShouldThrowException_WhenEmailIsNull() {
    IllegalArgumentException exception = assertThrows(
      IllegalArgumentException.class,
      () -> accountService.registerAccount(null, "username", "password")
    );
    assertEquals(
      "Must specify non-null email, username, and password to create an account.",
      exception.getMessage()
    );
  }

  @Test
  void registerAccount_ShouldThrowResourceConflictException_WhenEmailExists() {
    String email = "test@example.com";
    when(repository.findByEmail(email)).thenReturn(Optional.of(new Account()));

    assertThrows(ResourceConflictException.class, () ->
      accountService.registerAccount(email, "username", "password")
    );
  }

  @Test
  void registerAccount_ShouldSaveAccount_WhenValidInput() {
    String email = "test@example.com";
    String username = "testuser";
    String password = "password";
    String encodedPassword = "encodedPassword";

    when(repository.findByEmail(email)).thenReturn(Optional.empty());
    when(encoder.encode(password)).thenReturn(encodedPassword);
    when(repository.save(any(Account.class))).thenAnswer(invocation ->
      invocation.getArgument(0)
    );

    Account result = accountService.registerAccount(email, username, password);

    assertNotNull(result);
    assertEquals(email, result.getEmail());
    assertEquals(username, result.getUsername());
    assertEquals(encodedPassword, result.getPassword());
    verify(repository).save(any(Account.class));
  }

  @Test
  void getByEmail_ShouldThrowException_WhenEmailIsNull() {
    assertThrows(IllegalArgumentException.class, () ->
      accountService.getByEmail(null)
    );
  }

  @Test
  void getByEmail_ShouldThrowResourceNotFoundException_WhenAccountNotFound() {
    String email = "notfound@example.com";
    when(repository.findByEmail(email)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () ->
      accountService.getByEmail(email)
    );
  }

  @Test
  void getByEmail_ShouldReturnAccount_WhenAccountExists() {
    String email = "test@example.com";
    Account mockAccount = new Account(email, "username", "password");
    when(repository.findByEmail(email)).thenReturn(Optional.of(mockAccount));

    Account result = accountService.getByEmail(email);

    assertNotNull(result);
    assertEquals(mockAccount, result);
  }

  @Test
  void updateAccount_ShouldThrowUnauthorizedException_WhenUserIdMismatch() {
    Account account = new Account("test@example.com", "username", "password");
    account.setId("accountId");

    assertThrows(UnauthorizedException.class, () ->
      accountService.updateAccount(account, "differentUserId")
    );
  }

  @Test
  void updateAccount_ShouldUpdateAccount_WhenValidInput() {
    String userId = "userId";
    Account existingAccount = new Account(
      "email@example.com",
      "oldUsername",
      "oldPassword"
    );
    existingAccount.setId(userId);

    Account updateAccount = new Account(null, "newUsername", "newPassword");
    updateAccount.setId(userId);

    when(repository.findById(userId)).thenReturn(Optional.of(existingAccount));
    when(repository.save(any(Account.class))).thenAnswer(invocation ->
      invocation.getArgument(0)
    );

    Account result = accountService.updateAccount(updateAccount, userId);

    assertNotNull(result);
    assertEquals("newUsername", result.getUsername());
    assertEquals("newPassword", result.getPassword());
  }

  @Test
  void getById_ShouldThrowException_WhenIdIsNull() {
    assertThrows(IllegalArgumentException.class, () ->
      accountService.getById(null)
    );
  }

  @Test
  void getById_ShouldThrowResourceNotFoundException_WhenAccountNotFound() {
    String id = "nonexistentId";
    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () ->
      accountService.getById(id)
    );
  }

  @Test
  void getById_ShouldReturnAccount_WhenAccountExists() {
    String id = "accountId";
    Account mockAccount = new Account(
      "email@example.com",
      "username",
      "password"
    );
    mockAccount.setId(id);
    when(repository.findById(id)).thenReturn(Optional.of(mockAccount));

    Account result = accountService.getById(id);

    assertNotNull(result);
    assertEquals(mockAccount, result);
  }

  @Test
  void deleteAccount_ShouldThrowUnauthorizedException_WhenUserIdMismatch() {
    Account account = new Account("test@example.com", "username", "password");
    account.setId("accountId");

    assertThrows(UnauthorizedException.class, () ->
      accountService.deleteAccount(account, "differentUserId")
    );
  }

  @Test
  void deleteAccount_ShouldDeleteAccount_WhenValidInput() {
    Account account = new Account("test@example.com", "username", "password");
    account.setId("userId");

    accountService.deleteAccount(account, "adminId");

    verify(repository).delete(account);
  }
}
