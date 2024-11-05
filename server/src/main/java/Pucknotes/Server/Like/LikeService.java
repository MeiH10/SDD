package Pucknotes.Server.Like;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Account.AccountService;
import Pucknotes.Server.Note.Note;
import Pucknotes.Server.Note.NoteService;
import Pucknotes.Server.Response.Types.UnauthorizedException;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Document(collection = "likes")
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private NoteService noteService;

    // Like a note
    public void likeNote(String noteId, String userId) {
        Account user = accountService.getById(userId);
        Note note = noteService.getNote(noteId);

        // Check if the user has already liked the note
        Optional<Like> existingLike = likeRepository.findByUserAndNote(user, note);
        if (existingLike.isPresent()) {
            throw new UnauthorizedException("You have already liked this note.");
        }

        // Create and save a new Like
        Like like = new Like();
        like.setUser(user);
        like.setNote(note);
        likeRepository.save(like);
    }

    // Dislike a note (remove like)
    public void dislikeNote(String noteId, String userId) {
        Account user = accountService.getById(userId);
        Note note = noteService.getNote(noteId);

        // Find and remove the like
        Optional<Like> existingLike = likeRepository.findByUserAndNote(user, note);
        if (existingLike.isEmpty()) {
            throw new UnauthorizedException("You have not liked this note.");
        }

        likeRepository.delete(existingLike.get());
    }

    // Check if a user has liked a note
    public boolean hasLiked(String noteId, String userId) {
        Account user = accountService.getById(userId);
        Note note = noteService.getNote(noteId);
        return likeRepository.findByUserAndNote(user, note).isPresent();
    }
}

