package SDD.PuckNotes.Notes;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class NoteService   {

    @Autowired
    private NoteRepository NoteRepo;

    public String addNote(String title, MultipartFile file) throws IOException {
        Note note = new Note();
        note.setTitle(title);
        note.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        note = NoteRepo.insert(note);
        return note.getId();
    }

    public Note getNote(String id) {
        return NoteRepo.findById(id).get();
    }
}
