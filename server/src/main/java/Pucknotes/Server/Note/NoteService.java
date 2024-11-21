package Pucknotes.Server.Note;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.File.FileService;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Response.Types.UnauthorizedException;
import Pucknotes.Server.Section.Section;

import java.io.IOException;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository repository;

    @Autowired
    private FileService files;

    public Note createNote(Account user, Section section, String title, String description, MultipartFile upload, String link, List<String> tags) throws IOException {
        if (user == null) {
            throw new UnauthorizedException("You must log in to create a note.");
        } else if (section == null) {
            throw new ResourceNotFoundException("Specify a valid course.");
        }

        ObjectId fileID = files.addFile(upload);

        Note note = new Note();
        note.setCourse(section.getId());
        note.setOwner(user.getId());
        note.setTitle(title);
        note.setDescription(description);
        note.setLink(link);
        note.setTags(tags);
        note.setFile(fileID.toString());

        repository.save(note);
        return note;
    }

    public Note getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid account ID.");
        }

        Note note = repository.findById(id).orElse(null);
        if (note == null) {
            throw new ResourceNotFoundException("No account with this ID.");
        }

        return note;
    }

    // public void updateNote(String id, String title, MultipartFile file, String userID) throws IOException {
    //     Account account = accounts.getById(userID);
    //     Optional<Note> optionalNote = NoteRepo.findById(id);
    //     if (optionalNote.isPresent()) {
    //         Note note = optionalNote.get();
    //         if (!account.getId().equals(note.getOwner().getId())) {
    //             throw new UnauthorizedException("You are not the note's owner.");
    //         }
    //         if (title != null) {
    //             note.setTitle(title);
    //         }
    //         if (file != null && !file.isEmpty()) {
    //             note.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
    //         }
    //         NoteRepo.save(note);
    //     } else {
    //         throw new ResourceNotFoundException(id);
    //     }
    // }

    // // Method to get all note IDs
    // public List<String> getAllNoteIds() {
    //     return NoteRepo.findAll().stream().map(Note::getId).collect(Collectors.toList());
    // }

    // public void deleteNote(String id, String userID) {
    //     Optional<Note> optionalNote = NoteRepo.findById(id);
    //     if (optionalNote.isPresent()) {
    //         Note note = optionalNote.get();
    //         Account account = accounts.getById(userID);

    //         // Check if the user is the owner of the note
    //         if (!account.getId().equals(note.getOwner().getId())) {
    //             throw new UnauthorizedException("You are not the note's owner.");
    //         }

    //         // Perform the deletion
    //         NoteRepo.delete(note);
    //     } else {
    //         throw new ResourceNotFoundException(id);
    //     }
    // }
}
