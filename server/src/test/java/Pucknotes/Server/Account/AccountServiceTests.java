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
    
}
