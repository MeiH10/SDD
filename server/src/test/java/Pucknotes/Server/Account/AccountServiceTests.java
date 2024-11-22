package Pucknotes.Server.Account;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> accountService.registerAccount(null, "username", "password"));
        assertEquals("Must specify non-null email, username, and password to create an account.", exception.getMessage());
    }

    @Test
    void registerAccount_ShouldThrowResourceConflictException_WhenEmailExists() {
        String email = "test@example.com";
        when(repository.findByEmail(email)).thenReturn(Optional.of(new Account()));

        assertThrows(ResourceConflictException.class,
                () -> accountService.registerAccount(email, "username", "password"));
    }

    @Test
    void registerAccount_ShouldSaveAccount_WhenValidInput() {
        String email = "test@example.com";
        String username = "testuser";
        String password = "password";
        String encodedPassword = "encodedPassword";

        when(repository.findByEmail(email)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn(encodedPassword);
        when(repository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account result = accountService.registerAccount(email, username, password);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(username, result.getUsername());
        assertEquals(encodedPassword, result.getPassword());
        verify(repository).save(any(Account.class));
    }

    @Test
    void getByEmail_ShouldThrowException_WhenEmailIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> accountService.getByEmail(null));
    }

    @Test
    void getByEmail_ShouldThrowResourceNotFoundException_WhenAccountNotFound() {
        String email = "notfound@example.com";
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> accountService.getByEmail(email));
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


}
