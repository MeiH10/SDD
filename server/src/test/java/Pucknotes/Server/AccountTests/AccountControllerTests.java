package Pucknotes.Server.Account;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        Account mockAccount = new Account("test@example.com", "testUser", "password");
        mockAccount.setId("accountId");

        when(verificationService.verifyToken(registration, token)).thenReturn(mockAccount);
        when(accountService.registerAccount(mockAccount)).thenReturn(mockAccount);

        ResponseEntity<APIResponse<String>> response = accountController.createAccount(token, registration);

        assertNotNull(response);
        assertEquals("accountId", response.getBody().getData());
        verify(verificationService, times(1)).verifyToken(registration, token);
        verify(accountService, times(1)).registerAccount(mockAccount);
    }
    
}
