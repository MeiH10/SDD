package SDD.PuckNotes.Notes;

public class NoteNotFoundException extends RuntimeException {

    // Constructor that accepts a custom error message
    public NoteNotFoundException(String message) {
        super(message);
    }
}