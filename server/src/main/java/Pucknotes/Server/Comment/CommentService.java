package Pucknotes.Server.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Account.AccountService;
import Pucknotes.Server.Note.Note;
import Pucknotes.Server.Note.NoteService;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Response.Types.UnauthorizedException;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
@Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private NoteService noteServive;

    public Comment createComment(String userID, String noteID, String body) {
        if (userID == null) {
            throw new UnauthorizedException("User must be logged in to create a comment.");
        }
        
        Note note = noteServive.getNote(noteID);
        Account account = accountService.getById(userID);
        Comment comment = new Comment(account, note, body);
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByNoteId(String noteId) {
        return commentRepository.findByNoteId(noteId);
    }

    public Optional<Comment> getCommentById(String id) {
        return commentRepository.findById(id);
    }

    public Comment editComment(String commentID, String body, String userID) {
        Comment comment = commentRepository.findById(commentID).orElse(null);
        if (comment == null) {
            throw new ResourceNotFoundException("Comment not found.");
        }

        Account account = accountService.getById(userID);
        
        if (!comment.getAccount().getId().equals(account.getId())) {
            throw new UnauthorizedException("You are not the owner of this comment.");
        }

        comment.setDescription(body);
        return commentRepository.save(comment);
    }

    public void deleteComment(String commentID, String userID) {
        Comment comment = commentRepository.findById(commentID).orElse(null);
        if (comment == null) {
            throw new ResourceNotFoundException("Comment not found.");
        }

        Account account = accountService.getById(userID);
        
        if (!comment.getAccount().getId().equals(account.getId())) {
            throw new UnauthorizedException("You are not the owner of this comment.");
        }

        commentRepository.deleteById(commentID);
    }
}

