package Pucknotes.Server.Account;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Session.SessionService;
import Pucknotes.Server.Verification.VerificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class AccountControllerTest {

  private AccountController accountController;

  @Mock
  private AccountService accountService;

  @Mock
  private VerificationService verificationService;

  @Mock
  private SessionService sessionService;

  @Mock
  private HttpServletRequest request;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    accountController = new AccountController();
    accountController.service = accountService;
    accountController.verify_service = verificationService;
    accountController.sessionService = sessionService;
  }

  @Test
  void testCreateAccount() {
    String token = "validToken";
    String registration = "validRegistration";
    Account mockAccount = new Account(
      "test@example.com",
      "testUser",
      "password"
    );
    mockAccount.setId("accountId");

    when(verificationService.verifyToken(registration, token)).thenReturn(
      mockAccount
    );
    when(accountService.registerAccount(mockAccount)).thenReturn(mockAccount);

    ResponseEntity<APIResponse<String>> response =
      accountController.createAccount(token, registration);

    assertNotNull(response);
    assertEquals("accountId", response.getBody().getData());
    verify(verificationService, times(1)).verifyToken(registration, token);
    verify(accountService, times(1)).registerAccount(mockAccount);
  }

  @Test
  void testForceAccount() {
    String email = "test@example.com";
    String username = "testUser";
    String password = "securePassword";
    Account mockAccount = new Account(email, username, password);
    mockAccount.setId("forcedAccountId");

    when(accountService.registerAccount(any(Account.class))).thenReturn(
      mockAccount
    );

    ResponseEntity<APIResponse<String>> response =
      accountController.forceAccount(email, username, password);

    assertNotNull(response);
    assertEquals("forcedAccountId", response.getBody().getData());
    verify(accountService, times(1)).registerAccount(any(Account.class));
  }

  @Test
  void testGetAccount() {
    String accountId = "123";
    Account mockAccount = new Account(
      "test@example.com",
      "testUser",
      "password"
    );
    mockAccount.setId(accountId);

    when(accountService.getById(accountId)).thenReturn(mockAccount);

    ResponseEntity<APIResponse<Account>> response =
      accountController.getAccount(accountId);

    assertNotNull(response);
    assertEquals(accountId, response.getBody().getData().getId());
    verify(accountService, times(1)).getById(accountId);
  }

  @Test
  void testUpdateAccount() {
    String accountId = "123";
    String username = "newUsername";
    String password = "newPassword";
    String userID = "sessionUserID";

    Account mockAccount = new Account(
      "test@example.com",
      "oldUsername",
      "oldPassword"
    );
    mockAccount.setId(accountId);

    when(sessionService.getSession(request)).thenReturn(userID);
    when(accountService.getByEmail(accountId)).thenReturn(mockAccount);

    ResponseEntity<String> response = accountController.updateAccount(
      request,
      accountId,
      username,
      password
    );

    assertNotNull(response);
    assertEquals("Account updated successfully.", response.getBody());
    verify(accountService, times(1)).updateAccount(mockAccount, userID);
  }

  @Test
  void testDeleteAccount_Success() {
    String accountId = "123";
    String userID = "sessionUserID";
    Account mockAccount = new Account(
      "test@example.com",
      "testUser",
      "password"
    );
    mockAccount.setId(accountId);

    when(sessionService.getSession(request)).thenReturn(userID);
    when(accountService.getById(accountId)).thenReturn(mockAccount);

    ResponseEntity<APIResponse<Boolean>> response =
      accountController.deleteAccount(request, accountId);

    assertNotNull(response);
    assertTrue(response.getBody().getData());
    verify(accountService, times(1)).deleteAccount(mockAccount, userID);
  }

  @Test
  void testDeleteAccount_NotFound() {
    String accountId = "123";
    String userID = "sessionUserID";

    when(sessionService.getSession(request)).thenReturn(userID);
    when(accountService.getById(accountId)).thenThrow(
      new ResourceNotFoundException("Account not found")
    );

    ResponseEntity<APIResponse<Boolean>> response =
      accountController.deleteAccount(request, accountId);

    assertNotNull(response);
    assertFalse(response.getBody().getData());
    verify(accountService, times(1)).getById(accountId);
  }

  @Test
  void testUpdateAccountRole() {
    String accountId = "123";
    int newRole = 1;
    String userID = "sessionUserID";

    Account mockAccount = new Account(
      "test@example.com",
      "testUser",
      "password"
    );
    mockAccount.setId(accountId);

    when(sessionService.getSession(request)).thenReturn(userID);
    when(accountService.getByEmail(accountId)).thenReturn(mockAccount);

    ResponseEntity<String> response = accountController.updateAccountRole(
      request,
      accountId,
      newRole
    );

    assertNotNull(response);
    assertEquals("Account Role updated successfully.", response.getBody());
    verify(accountService, times(1)).updateAccountRole(
      mockAccount,
      userID,
      newRole
    );
  }
}
