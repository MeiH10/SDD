package Pucknotes.Server.Account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void testDefaultConstructor() {
        Account account = new Account();
        assertNull(account.getId());
        assertNull(account.getEmail());
        assertNull(account.getUsername());
        assertNull(account.getPassword());
        assertEquals(0, account.getRole());
    }

    @Test
    void testParameterizedConstructor() {
        String email = "test@example.com";
        String username = "testUser";
        String password = "securePassword";
        Account account = new Account(email, username, password, "FirstName", "LastName");

        assertEquals(email, account.getEmail());
        assertEquals(username, account.getUsername());
        assertEquals(password, account.getPassword());
    }

    @Test
    void testGetAndSetEmail() {
        Account account = new Account();
        String email = "test@example.com";

        account.setEmail(email);
        assertEquals(email, account.getEmail());
    }

    @Test
    void testGetAndSetUsername() {
        Account account = new Account();
        String username = "testUser";

        account.setUsername(username);
        assertEquals(username, account.getUsername());
    }

    @Test
    void testGetAndSetPassword() {
        Account account = new Account();
        String password = "securePassword";

        account.setPassword(password);
        assertEquals(password, account.getPassword());
    }

    @Test
    void testGetAndSetRole() {
        Account account = new Account();
        int newRole = 2;

        account.setRole(newRole);
        assertEquals(newRole, account.getRole());
    }

    @Test
    void testIdIsNullByDefault() {
        Account account = new Account();
        assertNull(account.getId());
    }

    @Test
    void testNonNullFieldsConstructor() {
        String email = "email@example.com";
        String username = "username";
        String password = "password";

        Account account = new Account(email, username, password, "First", "Last");
        assertEquals(email, account.getEmail());
        assertEquals(username, account.getUsername());
        assertEquals(password, account.getPassword());
    }
}
