package SDD.PuckNotes.Notes;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService {

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

  public void updateNote(String id, String title, MultipartFile file) throws IOException {
    Optional<Note> optionalNote = NoteRepo.findById(id);
    if (optionalNote.isPresent()) {
      Note note = optionalNote.get();
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
}
