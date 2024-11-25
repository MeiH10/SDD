package Pucknotes.Server.Note;

import org.apache.logging.log4j.util.InternalException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import Pucknotes.Server.Account.Account;
import Pucknotes.Server.Account.AccountService;
import Pucknotes.Server.Course.CourseService;
import Pucknotes.Server.File.File;
import Pucknotes.Server.File.FileService;
import Pucknotes.Server.Major.MajorService;
import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Response.Types.UnauthorizedException;
import Pucknotes.Server.School.SchoolService;
import Pucknotes.Server.Section.Section;
import Pucknotes.Server.Section.SectionService;
import Pucknotes.Server.Semester.SemesterService;
import Pucknotes.Server.Session.SessionService;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

/**
 * The NoteController class is responsible for managing notes within the application.
 * It handles HTTP requests related to notes, such as creating, retrieving,
 * updating, and deleting notes, as well as managing likes.
 */
@RestController
@RequestMapping("/api/note")
public class NoteController {

    @Autowired
    private NoteService notes;

    @Autowired
    private SectionService sections;

    @Autowired
    private CourseService courses;

    @Autowired
    private MajorService majors;

    @Autowired
    private SchoolService schools;

    @Autowired
    private SemesterService semesters;

    @Autowired
    private SessionService sessions;

    @Autowired
    private FileService files;

    @Autowired
    private AccountService accounts;

    /**
     * This method handles the HTTP POST request to add a new note.
     * It requires the title and sectionID parameters, and optionally accepts a file,
     * a link, a description, tags, and an anonymous flag.
     *
     * @param request the HTTP request
     * @param title the title of the note
     * @param description an optional description of the note
     * @param file an optional file associated with the note
     * @param link an optional link associated with the note
     * @param sectionID the ID of the section to which the note belongs
     * @param tags optional tags to categorize the note
     * @param anonymous a boolean flag indicating if the note is anonymous
     * @return a ResponseEntity containing the API response with the ID of the created note
     */
    @PostMapping("")
    public ResponseEntity<APIResponse<String>> addNote(
            HttpServletRequest request,
            @RequestParam(value = "title", required = true) String title,
            @RequestParam(value = "description", defaultValue = "") String description,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "link", required = false) String link,
            @RequestParam(value = "sectionID", required = true) String sectionID,
            @RequestParam(value = "tags", defaultValue = "") List<String> tags,
            @RequestParam(value = "anonymous", defaultValue = "false") boolean anonymous) {

        // Check if the section exists before creating the note.
        if (!sections.existsById(sectionID)) {
            throw new IllegalArgumentException("A section with 'sectionID' does not exist.");
        }

        // Retrieve the current user from the session.
        Account user = sessions.getCurrentUser(request);
        Section section = sections.getById(sectionID);

        try {
            // Create the note using the provided parameters.
            Note note = notes.createNote(user, section, title, description, file, link, tags, anonymous);
            return ResponseEntity.ok(APIResponse.good(note.getId()));
        } catch (Exception error) {
            // Log the error message for debugging purposes.
            throw new InternalException("Could not create note.");
        }    
    }

    /**
     * This method handles the HTTP GET request to retrieve notes based on various filters.
     *
     * @param query optional search query string used to filter notes
     * @param tags optional list of tags used to filter notes
     * @param userID optional ID of the note owner to filter notes
     * @param sectionNumber optional number of the section to filter notes
     * @param sectionID optional ID of the section to filter notes
     * @param courseCode optional code of the course to filter notes
     * @param courseID optional ID of the course to filter notes
     * @param majorCode optional code of the major to filter notes
     * @param majorID optional ID of the major to filter notes
     * @param schoolName optional name of the school to filter notes
     * @param schoolID optional ID of the school to filter notes
     * @param semesterName optional name of the semester to filter notes
     * @param semesterID optional ID of the semester to filter notes
     * @param sort field to sort the notes
     * @param order order of the sorted results
     * @param type the type of response object to return
     * @return a ResponseEntity containing the API response with the list of notes or their IDs
     */
    @GetMapping("")
    public ResponseEntity<APIResponse<Object>> getMajorsFull(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "tags", defaultValue = "") List<String> tags,
            @RequestParam(value = "ownerID", required = false) String userID,
            @RequestParam(value = "sectionNumber", required = false) String sectionNumber,
            @RequestParam(value = "sectionID", required = false) String sectionID,
            @RequestParam(value = "courseCode", required = false) String courseCode,
            @RequestParam(value = "courseID", required = false) String courseID,
            @RequestParam(value = "majorCode", required = false) String majorCode,
            @RequestParam(value = "majorID", required = false) String majorID,
            @RequestParam(value = "schoolName", required = false) String schoolName,
            @RequestParam(value = "schoolID", required = false) String schoolID,
            @RequestParam(value = "semesterName", required = false) String semesterName,
            @RequestParam(value = "semesterID", required = false) String semesterID,
            @RequestParam(value = "sort", defaultValue = "likes") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam(value = "return", defaultValue = "id") String type) {

        // Validate given parameters to ensure they refer to existing resources.
        if (sectionID != null && !sections.existsById(sectionID)) {
            throw new IllegalArgumentException("A section with 'sectionID' does not exist.");
        } else if (sectionNumber != null && !sections.existsByNumber(sectionNumber)) {
            throw new IllegalArgumentException("A section with 'sectionNumber' does not exist.");
        } else if (courseID != null && !courses.existsById(courseID)) {
            throw new IllegalArgumentException("A course with 'courseID' does not exist.");
        } else if (courseCode != null && !courses.existsByCode(courseCode)) {
            throw new IllegalArgumentException("A course with 'courseCode' does not exist.");
        } else if (majorID != null && !majors.existsById(majorID)) {
            throw new IllegalArgumentException("A major with 'majorID' does not exist.");
        } else if (majorCode != null && !majors.existsByCode(majorCode)) {
            throw new IllegalArgumentException("A major with 'majorName' does not exist.");
        } else if (schoolID != null && !schools.existsById(schoolID)) {
            throw new IllegalArgumentException("A school with 'schoolID' does not exist.");
        } else if (schoolName != null && !schools.existsByName(schoolName)) {
            throw new IllegalArgumentException("A school with 'schoolName' does not exist.");
        } else if (semesterID != null && !semesters.existsById(semesterID)) {
            throw new IllegalArgumentException("A semester with 'semesterID' does not exist.");
        } else if (semesterName != null && !semesters.existsByName(semesterName)) {
            throw new IllegalArgumentException("A semester with 'semesterName' does not exist.");
        } else if (userID != null && !accounts.existsById(userID)) {
            throw new ResourceNotFoundException("Account with 'userID' does not exist.");
        }
        
        // Map names to IDs when necessary for filtering.
        if (semesterID == null && semesterName != null) {
            semesterID = semesters.getByName(semesterName).getId();
        }

        if (schoolID == null && schoolName != null) {
            schoolID = schools.getByName(schoolName).getId();
        }

        if (majorID == null && majorCode != null) {
            majorID = majors.getByCode(majorCode).getId();
        }

        if (courseID == null && courseCode != null) {
            courseID = courses.getByCode(courseCode).getId();
        }

        if (sectionID == null && sectionNumber != null) {
            sectionID = sections.getByNumber(sectionNumber).getId();
        }

        // Retrieve notes using the provided filters.
        List<Note> result = notes.getNotes(sectionID, courseID, majorID, semesterID, schoolID, tags, query, sort, order, userID);
        
        // Anonymize the note by removing owner information if indicated.
        result.forEach(note -> {
            if (note.isAnonymous()) note.setOwner(null);
        });

        // Return the results based on the specified response type: either full objects, counts, or IDs.
        switch (type) {
            case "object":
                return ResponseEntity.ok(APIResponse.good(result));
            case "count":
                return ResponseEntity.ok(APIResponse.good(result.size()));
            default:
                List<String> ids = result.stream().map(Note::getId).toList();
                return ResponseEntity.ok(APIResponse.good(ids));
        }
    }

    /**
     * This method retrieves a specific note by its unique identifier.
     *
     * @param id the unique identifier of the note
     * @return a ResponseEntity containing the API response with the requested note
     */
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Note>> getNoteById(@PathVariable String id) {
        // Retrieve the note based on the provided ID.
        Note note = notes.getById(id);
        // Anonymize the note before sending it in the response.
        if (note.isAnonymous()) note.setOwner(null);
        return ResponseEntity.ok(APIResponse.good(note));
    }

    /**
     * This method retrieves the file associated with a specific note by its unique identifier.
     *
     * @param id the unique identifier of the note
     * @return a ResponseEntity containing the file content with appropriate HTTP headers
     * @throws IOException if there is an issue retrieving the file
     */
    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> getNoteFileById(@PathVariable String id) throws IOException {
        // Retrieve the note to access its associated file.
        Note note = notes.getById(id);
        File file = files.downloadFile(note.getFile());

        // Prepare the HTTP headers to return the file.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(file.getFileType());
        headers.setContentLength(file.getFileSize());

        // Return the file content as a byte array.
        return new ResponseEntity<byte[]>(file.getFile(), headers, HttpStatus.OK);
    }

    /**
     * This method handles the HTTP PUT request to update an existing note.
     * It allows the user to modify various attributes of the note.
     *
     * @param request the HTTP request
     * @param id the unique identifier of the note to update
     * @param file an optional new file to associate with the note
     * @param title optional new title of the note
     * @param description optional new description of the note
     * @param tags optional new list of tags for the note
     * @param sectionID optional new ID for the section to which the note belongs
     * @param anonymous optional new status for the anonymous flag
     * @return a ResponseEntity containing the API response with the ID of the updated note
     */
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<String>> updateNote(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String sectionID,
            @RequestParam(required = false) Boolean anonymous) {
        
        // Retrieve the current user from the session.
        Account user = sessions.getCurrentUser(request);
        Note note = notes.getById(id);

        // Only the owner can update a note.
        if (!user.getId().equals(note.getOwner())) {
            throw new UnauthorizedException("You must be a note's owner to edit it.");
        }

        // Update note attributes only if values are provided.
        if (title != null) {
            note.setTitle(title);
        }

        if (description != null) {
            note.setDescription(description);
        }
        
        if (tags != null) {
            note.setTags(tags);
        }

        if (anonymous != null) {
            note.setAnonymous(anonymous);
        }

        if (sectionID != null) {
            Section section = sections.getById(sectionID);

            note.setSection(section.getId());
            note.setCourse(section.getCourse());
            note.setMajor(section.getMajor());
            note.setSchool(section.getSchool());
            note.setSemester(section.getSemester());
        }

        // Handle file uploads and associate them with the note.
        try {
            if (file != null) {
                ObjectId fileID = files.addFile(file);
                note.setFile(fileID.toString());
            }
        } catch (IOException error) {
            // Throw an exception for a bad file type.
            throw new IllegalArgumentException("Bad file type.");
        }

        // Commit the updated note to the database.
        notes.updateNote(note);
        
        return ResponseEntity.ok(APIResponse.good(note.getId()));
    }

    /**
     * This method handles the HTTP DELETE request to remove a note by its unique identifier.
     *
     * @param request the HTTP request
     * @param id the unique identifier of the note to delete
     * @return a ResponseEntity containing the API response with the success status of the deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteNote(
            HttpServletRequest request,
            @PathVariable String id) {
        
        // Retrieve the current user from the session.
        Account user = sessions.getCurrentUser(request);
        Note note = notes.getById(id);

        // Only the owner can delete a note.
        if (!user.getId().equals(note.getOwner())) {
            throw new UnauthorizedException("You must be a note's owner to delete it.");
        }

        // Delete the note from the system.
        notes.deleteNote(note);

        return ResponseEntity.ok(APIResponse.good(true));
    }

    /**
     * This method handles the HTTP PUT request to like a note.
     * It allows users to like a note, thus increasing its like count.
     *
     * @param request the HTTP request
     * @param id the unique identifier of the note to like
     * @return a ResponseEntity containing the API response with the success status of the like action
     */
    @PutMapping("/{id}/like")
    public ResponseEntity<APIResponse<Boolean>> likeNote(
            HttpServletRequest request,
            @PathVariable String id) {
        
        // Retrieve the current user from the session.
        Account user = sessions.getCurrentUser(request);
        Note note = notes.getById(id);

        // Like the note on behalf of the user.
        notes.likeNote(user, note);

        return ResponseEntity.ok(APIResponse.good(true));
    }

    /**
     * This method checks if the current user has liked a specific note.
     * It returns a boolean indicating the like status.
     *
     * @param request the HTTP request
     * @param id the unique identifier of the note to check
     * @return a ResponseEntity containing the API response with the like status
     */
    @GetMapping("/{id}/like")
    public ResponseEntity<APIResponse<Boolean>> hasLikedNote(
            HttpServletRequest request,
            @PathVariable String id) {
        
        // Retrieve the current user from the session.
        Account user = sessions.getCurrentUser(request);
        Note note = notes.getById(id);

        // Return whether the user has liked the note or not.
        return ResponseEntity.ok(APIResponse.good(notes.hasLikedNote(user, note)));
    }

    /**
     * This method handles the HTTP DELETE request to dislike a note.
     * It allows users to remove their like from a note, decreasing its like count.
     *
     * @param request the HTTP request
     * @param id the unique identifier of the note to dislike
     * @return a ResponseEntity containing the API response with the success status of the dislike action
     */
    @DeleteMapping("/{id}/like")
    public ResponseEntity<APIResponse<Boolean>> dislikeNote(
            HttpServletRequest request,
            @PathVariable String id) {
        
        // Retrieve the current user from the session.
        Account user = sessions.getCurrentUser(request);
        Note note = notes.getById(id);

        // Dislike the note on behalf of the user.
        notes.dislikeNote(user, note);

        return ResponseEntity.ok(APIResponse.good(false));
    }
}
