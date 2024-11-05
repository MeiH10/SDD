package Pucknotes.Server.Note;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Account.AccountService;
import Pucknotes.Server.Course.Course;
import Pucknotes.Server.Course.CourseService;
import Pucknotes.Server.Response.Types.UnauthorizedException;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Document(collection = "notes")
public class NoteService {

    @Autowired
    private NoteRepository NoteRepo;

    @Autowired
    private AccountService accounts;

    @Autowired
    private CourseService courses;

    public String addNote(String title, MultipartFile file, String userID, String courseID) throws IOException {
        Account account = accounts.getById(userID);
        Course course = courses.getById(courseID);
        Note note = new Note(title, course, account, new Binary(BsonBinarySubType.BINARY, file.getBytes()));
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
