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
import Pucknotes.Server.Course.CourseService;
import Pucknotes.Server.File.File;
import Pucknotes.Server.File.FileService;
import Pucknotes.Server.Like.LikeService;
import Pucknotes.Server.Major.MajorService;
import Pucknotes.Server.Note.Note.Statistics;
import Pucknotes.Server.Response.APIResponse;
import Pucknotes.Server.Response.Types.UnauthorizedException;
import Pucknotes.Server.School.SchoolService;
import Pucknotes.Server.Section.Section;
import Pucknotes.Server.Section.SectionService;
import Pucknotes.Server.Semester.SemesterService;
import Pucknotes.Server.Session.SessionService;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

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
    private LikeService likes;

    @Autowired
    private FileService files;

    // Endpoint to add a new note
    @PostMapping("")
    public ResponseEntity<APIResponse<String>> addNote(
            HttpServletRequest request,
            @RequestParam(value = "title", required = true) String title,
            @RequestParam(value = "description", defaultValue = "") String description,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "link", required = false) String link,
            @RequestParam(value = "sectionID", required = true) String sectionID,
            @RequestParam(value = "tags", defaultValue = "") List<String> tags) {

        if (!sections.existsById(sectionID)) {
            throw new IllegalArgumentException("A section with 'sectionID' does not exist.");
        }

        Account user = sessions.getCurrentUser(request);
        Section section = sections.getById(sectionID);

        try {
            Note note = notes.createNote(user, section, title, description, file, link, tags);
            return ResponseEntity.ok(APIResponse.good(note.getId()));
        } catch (Exception error) {
            throw new InternalException("Could not create note.");
        }    
    }

    @GetMapping("")
    public ResponseEntity<APIResponse<Object>> getMajorsFull(
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "tags", defaultValue = "") List<String> tags,
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
            @RequestParam(value = "sort", defaultValue = "title") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam(value = "return", defaultValue = "id") String type) {

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
        }
        
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

        List<Note> result = notes.getNotes(sectionID, courseID, majorID, semesterID, schoolID, tags, query, sort, order);

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

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Note>> getNoteById(@PathVariable String id) {
        Note note = notes.getById(id);
        return ResponseEntity.ok(APIResponse.good(note));
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> getNoteFileById(@PathVariable String id) throws IOException {

        Note note = notes.getById(id);
        File file = files.downloadFile(note.getFile());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(file.getFileType());
        headers.setContentLength(file.getFileSize());

        return new ResponseEntity<byte[]>(file.getFile(), headers, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<String>> updateNote(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String sectionID) {
        
        Account user = sessions.getCurrentUser(request);
        Note note = notes.getById(id);

        if (!user.getId().equals(note.getOwner())) {
            throw new UnauthorizedException("You must be a note's owner to edit it.");
        }

        if (title != null) {
            note.setTitle(title);
        }

        if (description != null) {
            note.setDescription(description);
        }
        
        if (tags != null) {
            note.setTags(tags);
        }

        if (sectionID != null) {
            Section section = sections.getById(sectionID);

            note.setSection(section.getId());
            note.setCourse(section.getCourse());
            note.setMajor(section.getMajor());
            note.setSchool(section.getSchool());
            note.setSemester(section.getSemester());
        }

        try {
            if (file != null) {
                ObjectId fileID = files.addFile(file);
                note.setFile(fileID.toString());
            }
        } catch (IOException error) {
            throw new IllegalArgumentException("Bad file type.");
        }

        notes.updateNote(note);
        
        return ResponseEntity.ok(APIResponse.good(note.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Boolean>> deleteNote(
            HttpServletRequest request,
            @PathVariable String id) {
        
        Account user = sessions.getCurrentUser(request);
        Note note = notes.getById(id);

        if (!user.getId().equals(note.getOwner())) {
            throw new UnauthorizedException("You must be a note's owner to delete it.");
        }

        notes.deleteNote(note);

        return ResponseEntity.ok(APIResponse.good(true));
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<APIResponse<Boolean>> likeNote(
            HttpServletRequest request,
            @PathVariable String id) {

        Account user = sessions.getCurrentUser(request);
        Note note = notes.getById(id);
        likes.likeNote(user, note);

        return ResponseEntity.ok(APIResponse.good(true));
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<APIResponse<Boolean>> hasLikedNote(
            HttpServletRequest request,
            @PathVariable String id) {
        
        Account user = sessions.getCurrentUser(request);
        Note note = notes.getById(id);

        return ResponseEntity.ok(APIResponse.good(likes.hasLikedNote(user, note)));
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<APIResponse<Boolean>> dislikeNote(
            HttpServletRequest request,
            @PathVariable String id) {
        
        Account user = sessions.getCurrentUser(request);
        Note note = notes.getById(id);
        likes.dislikeNote(user, note);

        return ResponseEntity.ok(APIResponse.good(false));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<APIResponse<Statistics>> getStaistics(
            HttpServletRequest request,
            @PathVariable String id) {
        
        Note note = notes.getById(id);
        var stats = new Statistics(likes.totalNoteLikes(note));

        return ResponseEntity.ok(APIResponse.good(stats));
    }
}