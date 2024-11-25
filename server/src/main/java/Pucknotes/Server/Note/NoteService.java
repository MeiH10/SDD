package Pucknotes.Server.Note;

import java.io.IOException;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.File.FileService;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Response.Types.UnauthorizedException;
import Pucknotes.Server.Section.Section;
import java.util.ArrayList;

/**
 * Service class that provides various functionalities for handling notes.
 * It interacts with the Note repository and manages note-related operations
 * such as creating, fetching, updating, liking, disliking, and deleting notes.
 */
@Service
public class NoteService {

    @Autowired
    private NoteRepository repository;

    @Autowired
    private FileService files;

    @Autowired
    private MongoTemplate template;

    /**
     * Creates a new note associated with the specified user and section.
     *
     * @param user The account of the user creating the note.
     * @param section The section to which the note belongs.
     * @param title The title of the note.
     * @param description The description of the note.
     * @param upload An optional file upload associated with the note.
     * @param link A link associated with the note.
     * @param tags A list of tags associated with the note.
     * @param anonymous A boolean indicating whether the note should be created anonymously.
     * @return The created Note object.
     * @throws IOException If the file upload fails.
     * @throws UnauthorizedException If the user is not logged in.
     * @throws ResourceNotFoundException If the specified section is invalid.
     */
    public Note createNote(Account user, Section section, String title, String description, MultipartFile upload,
                           String link, List<String> tags, boolean anonymous) throws IOException {
        if (user == null) {
            throw new UnauthorizedException("You must log in to create a note.");
        } else if (section == null) {
            throw new ResourceNotFoundException("Specify a valid course.");
        }

        if(user.getRole() == 0 || user.getRole() == 1){
            throw new UnauthorizedException("You are not permitted to add a note.");
        }

        ObjectId fileID = files.addFile(upload);

        Note note = new Note();
        
        // Setting the properties of the note using the provided information.
        note.setSection(section.getId());
        note.setCourse(section.getCourse());
        note.setMajor(section.getMajor());
        note.setSchool(section.getSchool());
        note.setSemester(section.getSemester());

        note.setOwner(user.getId());
        note.setTitle(title);
        note.setDescription(description);
        note.setLink(link);
        note.setTags(tags);
        note.setFile(fileID.toString());
        note.setAnonymous(anonymous);

        // Save the note to the repository.
        repository.save(note);
        return note;
    }

    /**
     * Retrieves a note by its ID.
     *
     * @param id The ID of the note to retrieve.
     * @return The Note object associated with the specified ID.
     * @throws IllegalArgumentException If the provided ID is null.
     * @throws ResourceNotFoundException If no note exists with the specified ID.
     */
    public Note getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid note ID.");
        }

        var query = Query.query(Criteria.where("_id").is(new ObjectId(id)));
        query.fields().exclude("likes");

        Note note = template.findOne(query, Note.class);
        if (note == null) {
            throw new ResourceNotFoundException("No note with this ID.");
        }

        return note;
    }

    /**
     * Checks if a note exists by its ID.
     *
     * @param id The ID of the note to check.
     * @return True if the note exists, false otherwise.
     */
    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    /**
     * Retrieves a list of notes filtered by various criteria.
     *
     * @param sectionID The ID of the section.
     * @param courseID The ID of the course.
     * @param majorID The ID of the major.
     * @param semesterID The ID of the semester.
     * @param schoolID The ID of the school.
     * @param tags A list of tags to filter by.
     * @param search A search string for filtering by title or description.
     * @param sortType The criteria to sort the notes by (e.g., title, date).
     * @param orderType The order in which to sort the notes (ascending or descending).
     * @param ownerID The ID of the note owner.
     * @return A list of notes that match the specified criteria.
     */
    public List<Note> getNotes(
            String sectionID, String courseID, String majorID,
            String semesterID, String schoolID, List<String> tags,
            String search, String sortType, String orderType, String ownerID) {

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
            default:
                // Optionally log or handle unsupported sort types.
                break;
        }

        if (!tags.isEmpty()) {
            query.addCriteria(Criteria.where("tags").in(tags));
        }

        if (search != null) {
            query.addCriteria(Criteria.where("title").regex(search, "i")
                .orOperator(Criteria.where("description").regex(search, "i")));
        }

        if (courseID != null) {
            query.addCriteria(Criteria.where("course").is(new ObjectId(courseID)));
        }

        if (majorID != null) {
            query.addCriteria(Criteria.where("major").is(new ObjectId(majorID)));
        }

        if (semesterID != null) {
            query.addCriteria(Criteria.where("semester").is(new ObjectId(semesterID)));
        }

        if (schoolID != null) {
            query.addCriteria(Criteria.where("school").is(new ObjectId(schoolID)));
        }

        if (ownerID != null) {
            query.addCriteria(Criteria.where("account").is(new ObjectId(ownerID)));
        }

        query.fields().exclude("likes");

        return template.find(query, Note.class); // Fetch and return the filtered notes.
    }

    /**
     * Updates the properties of an existing note.
     *
     * @param note The Note object containing updated information.
     */
    public void updateNote(Note note, Account user) {
        if (!user.getId().equals(note.getOwner()) && user.getRole() != 3) {
            throw new UnauthorizedException("You are not the note's owner.");
        }

        repository.save(note);
    }

    /**
     * Deletes the specified note and its associated file.
     *
     * @param note The Note object to delete.
     */
    public void deleteNote(Note note, Account account) {
        if (!account.getId().equals(note.getOwner()) && account.getRole() != 3) {
            throw new UnauthorizedException("You are not the note's owner.");
        }
        repository.delete(note);
        files.deleteFile(note.getFile());
    }

    /**
     * Likes a note for the specified user.
     *
     * @param user The account of the user liking the note.
     * @param note The Note object to like.
     */
    public void likeNote(Account user, Note note) {
        if (note.getLikes().contains(user.getId())) return; // Do not allow duplicate likes.
        
        ArrayList<String> next = new ArrayList<>(note.getLikes());
        next.add(user.getId());
        note.setLikes(next);

        note.setTotalLikes(note.getTotalLikes() + 1); // Increment the total likes count.

        repository.save(note); // Save the updated note with the new like information.
    }

    /**
     * Dislikes a note for the specified user.
     *
     * @param user The account of the user disliking the note.
     * @param note The Note object to dislike.
     */
    public void dislikeNote(Account user, Note note) {
        if (!note.getLikes().contains(user.getId())) return; // Do not proceed if the user did not like the note.
    
        ArrayList<String> next = new ArrayList<>(note.getLikes());
        next.remove(user.getId());
        note.setLikes(next);

        note.setTotalLikes(note.getTotalLikes() - 1); // Decrement the total likes count.

        repository.save(note); // Save the updated note with the new dislike information.
    }

    /**
     * Checks if the specified user has liked the given note.
     *
     * @param user The account of the user.
     * @param note The Note object to check.
     * @return True if the user has liked the note, false otherwise.
     */
    public boolean hasLikedNote(Account user, Note note) {
        return note.getLikes().contains(user.getId()); // Check if the user's ID is in the list of likes.
    }
}
