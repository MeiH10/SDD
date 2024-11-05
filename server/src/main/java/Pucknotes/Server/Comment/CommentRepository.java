package Pucknotes.Server.Comment;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByNoteId(String noteId);
    List<Comment> findByAccountId(String accountId);
    Optional<Comment> findById(String id);
}