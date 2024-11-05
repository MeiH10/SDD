package Pucknotes.Server.Note;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Account.AccountService;
import Pucknotes.Server.Response.Types.UnauthorizedException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService {

    @Autowired
    private NoteRepository NoteRepo;

    @Autowired
    private AccountService accounts;

    public String addNote(String title, MultipartFile file, String userID) throws IOException {
        Account account = accounts.getById(userID);
        Note note = new Note(title, account, new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        note = NoteRepo.insert(note);
        return note.getId();
    }

    public Note getNote(String id) {
        return NoteRepo.findById(id).get();
    }

    public void updateNote(String id, String title, MultipartFile file, String userID) throws IOException {
        Account account = accounts.getById(userID);
        Optional<Note> optionalNote = NoteRepo.findById(id);
        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            if (!account.getId().equals(note.getOwner().getId())) {
                throw new UnauthorizedException("You are not the note's owner.");
            }
            if (title != null) {
                note.setTitle(title);
            }
            if (file != null && !file.isEmpty()) {
                note.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
            }
            NoteRepo.save(note);
        } else {
            throw new NoteNotFoundException(id);
        }
    }

    // Method to get all note IDs
    public List<String> getAllNoteIds() {
        return NoteRepo.findAll().stream().map(Note::getId).collect(Collectors.toList());
    }

    public void deleteNote(String id, String userID) {
        Optional<Note> optionalNote = NoteRepo.findById(id);
        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            Account account = accounts.getById(userID);

            // Check if the user is the owner of the note
            if (!account.getId().equals(note.getOwner().getId())) {
                throw new UnauthorizedException("You are not the note's owner.");
            }

            // Perform the deletion
            NoteRepo.delete(note);
        } else {
            throw new NoteNotFoundException(id);
        }
    }
}
