package Pucknotes.Server.Like;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Note.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface LikeRepository extends MongoRepository<Like, String> {

    // Find a like by user and note
    Optional<Like> findByUserAndNote(Account user, Note note);
}

