file://<WORKSPACE>/src/main/java/Pucknotes/Server/Note/NoteService.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 2592
uri: file://<WORKSPACE>/src/main/java/Pucknotes/Server/Note/NoteService.java
text:
```scala
package Pucknotes.Server.Note;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Account.AccountService;
import Pucknotes.Server.Response.Types.UnauthorizedException;

@Service
public class NoteService {

    @Autowired
    private NoteRepository NoteRepo;

    @Autowired
    private AccountService accounts;

    public String addNote(String title, MultipartFile file, String userID) throws IOException {
        Account account = accounts.getById(userID);

        if(account.getRole() == 0 || account.getRole() == 1){
            throw new UnauthorizedException("You are not permitted to add a note.");
        }

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
            if (!account.getId().equals(note.getOwner().getId()) && account.getRole() != 3) {
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
            i@@f (!account.getId().equals(note.getOwner().getId()) && account.getRole() != 3) {
                throw new UnauthorizedException("You are not the note's owner.");
            }

            // Perform the deletion
            NoteRepo.delete(note);
        } else {
            throw new NoteNotFoundException(id);
        }
    }
}

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:935)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:164)
	dotty.tools.pc.MetalsDriver.run(MetalsDriver.scala:45)
	dotty.tools.pc.HoverProvider$.hover(HoverProvider.scala:40)
	dotty.tools.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:376)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator