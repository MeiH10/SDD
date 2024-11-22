package Pucknotes.Server.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Note.Note;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Response.Types.UnauthorizedException;

import java.util.List;
import java.util.ArrayList;

@Service
public class CommentService {
    @Autowired
    private CommentRepository repository;

    @Autowired
    private MongoTemplate template;

    public Comment createComment(Note note, Account account, String body) {
        if (account == null) {
            throw new UnauthorizedException("User must be logged in to create a comment.");
        }

        Comment comment = new Comment(account.getId(), note.getId(), body);
        return repository.save(comment);
    }

    public List<Comment> getCommentsByNoteId(String noteId) {
        return repository.findByNote(noteId);
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

        var query = Query.query(Criteria.where("id").is(id));
        query.fields().exclude("likes");

        Comment comment = template.findOne(query, Comment.class);
        if (comment == null) {
            throw new ResourceNotFoundException("No course with this ID.");
        }

        return comment;
    }

    public List<Comment> getComments(String noteID, String userID, String sortType, String orderType) {
        Query query = new Query();

        Sort.Direction direction = "asc".equalsIgnoreCase(orderType)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        switch (sortType.toLowerCase()) {
            case "title":
                query.with(Sort.by(direction, "title"));
                break;
            case "semester":
                query.with(Sort.by(direction, "semester.year"));
                break;
            case "likes":
                query.with(Sort.by(direction, "totalLikes"));
                break;
            case "date":
                query.with(Sort.by(direction, "date"));
                break;
        }

        if (noteID != null) {
            query.addCriteria(Criteria.where("note").is(noteID));
        }

        if (userID != null) {
            query.addCriteria(Criteria.where("account").is(userID));
        }

        query.fields().exclude("likes");

        return template.find(query, Comment.class);
    }

    public void like(Account user, Comment comment) {
        if (comment.getLikes().contains(user.getId())) return;
    
        ArrayList<String> next = new ArrayList<String>(comment.getLikes());
        next.add(user.getId());
        comment.setLikes(next);

        comment.setTotalLikes(comment.getTotalLikes() + 1);

        repository.save(comment);
    }

    public void dislike(Account user, Comment comment) {
        if (!comment.getLikes().contains(user.getId())) return;
    
        ArrayList<String> next = new ArrayList<String>(comment.getLikes());
        next.remove(user.getId());
        comment.setLikes(next);

        comment.setTotalLikes(comment.getTotalLikes() - 1);

        repository.save(comment);
    }

    public boolean hasLiked(Account user, Comment comment) {
        return comment.getLikes().contains(comment.getId());
    }
}

