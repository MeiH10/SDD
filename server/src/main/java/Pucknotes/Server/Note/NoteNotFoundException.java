package Pucknotes.Server.Note;

public class NoteNotFoundException extends RuntimeException {

    // Constructor that accepts a custom error message
    public NoteNotFoundException(String message) {
        super(message);
    }
}