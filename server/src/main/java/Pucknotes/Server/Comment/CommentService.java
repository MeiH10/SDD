package Pucknotes.Server.Comment;

import org.bson.types.ObjectId;
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

/**
 * The CommentService class is responsible for handling comment-related operations
 * within the application. It facilitates creating, editing, deleting, retrieving, 
 * and liking comments associated with notes in the system.
 */
@Service
public class CommentService {
    
    @Autowired
    private CommentRepository repository;

    @Autowired
    private MongoTemplate template;

    /**
     * Creates a new comment associated with a specified note and user.
     * 
     * @param note  The note to which the comment belongs.
     * @param account The account of the user creating the comment.
     * @param body  The content of the comment.
     * @return The created Comment object.
     * @throws UnauthorizedException if the user is not logged in (account is null).
     */
    public Comment createComment(Note note, Account account, String body) {
        if (account == null) {
            throw new UnauthorizedException("User must be logged in to create a comment.");
        }

        int accountRole = account.getRole();
        if(accountRole == 0 || accountRole == 1){
            throw new UnauthorizedException("User deos not have the correct permissions to post a comment");
        }


        Comment comment = new Comment(account.getId(), note.getId(), body);
        return repository.save(comment);
    }

    /**
     * Retrieves a list of comments associated with a specific note.
     *
     * @param noteId The ID of the note for which comments are to be retrieved.
     * @return A list of Comment objects associated with the specified note.
     */
    public List<Comment> getCommentsByNoteId(String noteId) {
        return repository.findByNote(noteId);
    }

    /**
     * Edits an existing comment if the user is the owner of the comment.
     *
     * @param commentID The ID of the comment to edit.
     * @param body      The new content for the comment.
     * @param account   The account of the user attempting to edit the comment.
     * @return The updated Comment object.
     * @throws ResourceNotFoundException if the specified comment does not exist.
     * @throws UnauthorizedException if the user is not the owner of the comment.
     */
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

    /**
     * Deletes a comment if the user is the owner of the comment.
     *
     * @param comment The Comment object to be deleted.
     * @param user    The account of the user attempting to delete the comment.
     * @throws ResourceNotFoundException if the comment does not exist.
     * @throws UnauthorizedException if the user is not the owner of the comment.
     */
    public void deleteComment(Comment comment, Account user) {
        if (comment == null) {
            throw new ResourceNotFoundException("Comment not found.");
        }

        if (!comment.getAccount().equals(user.getId()) && user.getRole() != 3) {
            throw new UnauthorizedException("You are not the owner of this comment.");
        }

        repository.deleteById(comment.getId());
    }

    /**
     * Retrieves a comment by its ID.
     *
     * @param id The ID of the comment to retrieve.
     * @return The Comment object associated with the specified ID.
     * @throws IllegalArgumentException if the provided ID is null.
     * @throws ResourceNotFoundException if no comment exists with the specified ID.
     */
    public Comment getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid comment ID.");
        }

        var query = Query.query(Criteria.where("_id").is(new ObjectId(id)));

        Comment comment = template.findOne(query, Comment.class);
        if (comment == null) {
            throw new ResourceNotFoundException("No comment with this ID.");
        }

        return comment;
    }

    /**
     * Retrieves a list of comments based on specified filtering and sorting criteria.
     *
     * @param noteID   The ID of the note for which comments are to be retrieved, or null for all comments.
     * @param userID   The ID of the user for which comments are to be retrieved, or null for all.
     * @param sortType The field by which to sort the comments.
     * @param orderType The order of the sorting (ascending or descending).
     * @return A list of Comment objects fulfilling the specified criteria.
     */
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
            // More cases can be added here for additional sorting options.
        }

        if (noteID != null) {
            query.addCriteria(Criteria.where("note").is(new ObjectId(noteID)));
        }

        if (userID != null) {
            query.addCriteria(Criteria.where("account").is(new ObjectId(userID)));
        }

        query.fields().exclude("likes");

        return template.find(query, Comment.class);
    }

    /**
     * Adds a like from a user to a specified comment.
     *
     * @param user    The account of the user liking the comment.
     * @param comment The Comment object to which the like is to be added.
     */
    public void like(Account user, Comment comment) {
        // If the user has already liked the comment, there is no need to process further.
        if (comment.getLikes().contains(user.getId())) return;
    
        ArrayList<String> next = new ArrayList<String>(comment.getLikes());
        next.add(user.getId());
        comment.setLikes(next);
        
        // Increment the total likes for the comment.
        comment.setTotalLikes(comment.getTotalLikes() + 1);

        repository.save(comment); // Persist the updated comment.
    }

    /**
     * Removes a like from a user on a specified comment.
     *
     * @param user    The account of the user disliking the comment.
     * @param comment The Comment object from which the like is to be removed.
     */
    public void dislike(Account user, Comment comment) {
        // If the user has not liked the comment, there is no need to process.
        if (!comment.getLikes().contains(user.getId())) return;
    
        ArrayList<String> next = new ArrayList<String>(comment.getLikes());
        next.remove(user.getId());
        comment.setLikes(next);
        
        // Decrement the total likes for the comment.
        comment.setTotalLikes(comment.getTotalLikes() - 1);

        repository.save(comment); // Persist the updated comment.
    }

    /**
     * Checks if a user has liked a specified comment.
     *
     * @param user    The account of the user for whom to check the like status.
     * @param comment The Comment object to check.
     * @return True if the user has liked the comment, otherwise false.
     */
    public boolean hasLiked(Account user, Comment comment) {
        return comment.getLikes().contains(comment.getId()); // Check if the list of likes contains the user's ID.
    }
}
