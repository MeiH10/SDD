package Pucknotes.Server.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Note.Note;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Response.Types.UnauthorizedException;

import java.util.List;

@Service
public class CommentService {
@Autowired
    private CommentRepository repository;

    public Comment createComment(Note note, Account account, String body) {
        if (account == null) {
            throw new UnauthorizedException("User must be logged in to create a comment.");
        }

        Comment comment = new Comment(account.getId(), note.getId(), body);
        return repository.save(comment);
    }

    public List<Comment> getCommentsByNoteId(String noteId) {
        return repository.findByNoteId(noteId);
    }

    public Comment getCommentById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid account ID.");
        }

        Comment comment = repository.findById(id).orElse(null);
        if (comment == null) {
            throw new ResourceNotFoundException("No account with this ID.");
        }

        return comment;
    }

    public Comment editComment(String commentID, String body, Account account) {
        Comment comment = repository.findById(commentID).orElse(null);
        if (comment == null) {
            throw new ResourceNotFoundException("Comment not found.");
        }
        
        if (!comment.getAccount().equals(account.getId())) {
            throw new UnauthorizedException("You are not the owner of this comment.");
        }

        comment.setDescription(body);
        return repository.save(comment);
    }

    public void deleteComment(Comment comment, Account user) {
        if (comment == null) {
            throw new ResourceNotFoundException("Comment not found.");
        }
        
        if (!comment.getAccount().equals(user.getId())) {
            throw new UnauthorizedException("You are not the owner of this comment.");
        }

        repository.deleteById(comment.getId());
    }

    public Comment getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid course ID.");
        }

        Comment comment = repository.findById(id).orElse(null);
        if (comment == null) {
            throw new ResourceNotFoundException("No course with this ID.");
        }

        return comment;
    }
}

