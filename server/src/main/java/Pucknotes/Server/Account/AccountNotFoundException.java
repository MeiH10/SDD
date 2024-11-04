package Pucknotes.Server.Account;

public class AccountNotFoundException extends RuntimeException {

    // Constructor that accepts a custom error message
    public AccountNotFoundException(String message) {
        super(message);
    }
}