package Pucknotes.Server.Like;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Comment.Comment;
import Pucknotes.Server.Note.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private LikeRepository repository;

    @Autowired
    private MongoTemplate template;

    public Like getLikeNote(Account user, Note note) {
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is("note"));
        query.addCriteria(Criteria.where("user").is(user.getId()));
        query.addCriteria(Criteria.where("item").is(note.getId()));

        return template.findOne(query, Like.class);
    }

    public Like likeNote(Account user, Note note) {
        Like like = getLikeNote(user, note);
        if (like != null) return like;

        Like new_like = new Like(user.getId(), "note", note.getId());
        repository.save(new_like);

        return new_like;
    }

    public void dislikeNote(Account user, Note note) {
        Like like = getLikeNote(user, note);
        if (like == null) return;

        repository.delete(like);
    }

    public boolean hasLikedNote(Account user, Note note) {
        return getLikeNote(user, note) != null;
    }

    public long totalNoteLikes(Note note) {
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is("note"));
        query.addCriteria(Criteria.where("item").is(note.getId()));

        return template.count(query, Like.class);
    }
    
    public Like getLikeComment(Account user, Comment comment) {
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is("comment"));
        query.addCriteria(Criteria.where("user").is(user.getId()));
        query.addCriteria(Criteria.where("item").is(comment.getId()));

        return template.findOne(query, Like.class);
    }

    public Like likeComment(Account user, Comment comment) {
        Like like = getLikeComment(user, comment);
        if (like != null) return like;

        Like new_like = new Like(user.getId(), "comment", comment.getId());
        repository.save(new_like);

        return new_like;
    }

    public void dislikeComment(Account user, Comment comment) {
        Like like = getLikeComment(user, comment);
        if (like == null) return;

        repository.delete(like);
    }

    public boolean hasLikedComment(Account user, Comment comment) {
        return getLikeComment(user, comment) != null;
    }

    public long totalCommentLikes(Comment comment) {
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is("comment"));
        query.addCriteria(Criteria.where("item").is(comment.getId()));

        return template.count(query, Like.class);
    }
}

