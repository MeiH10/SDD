package Pucknotes.Server.Comment;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The CommentRepository interface is a Spring Data repository for managing Comment entities.
 * It extends the MongoRepository interface, providing basic CRUD functionality for Comment objects
 * stored in a MongoDB database.
 */
@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    /**
     * Retrieves a list of Comment entities associated with the specified note.
     *
     * @param note the identifier of the note for which comments are to be fetched
     * @return a List of Comment objects that are linked to the specified note
     */
    List<Comment> findByNote(String note);

    /**
     * Retrieves a list of Comment entities associated with the specified account.
     *
     * @param account the identifier of the account for which comments are to be fetched
     * @return a List of Comment objects that are associated with the specified account
     */
    List<Comment> findByAccount(String account);

    /**
     * Retrieves an optional Comment entity by its unique identifier.
     *
     * @param id the unique identifier of the Comment to be fetched
     * @return an Optional containing the Comment if found, otherwise an empty Optional
     */
    Optional<Comment> findById(String id);
}
