package Pucknotes.Server.Note;

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
import java.io.IOException;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository repository;

    @Autowired
    private FileService files;

    @Autowired
    private MongoTemplate template;

    public Note createNote(Account user, Section section, String title, String description, MultipartFile upload,
            String link, List<String> tags) throws IOException {
        if (user == null) {
            throw new UnauthorizedException("You must log in to create a note.");
        } else if (section == null) {
            throw new ResourceNotFoundException("Specify a valid course.");
        }

        ObjectId fileID = files.addFile(upload);

        Note note = new Note();

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

        repository.save(note);
        return note;
    }

    public Note getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid account ID.");
        }

        Note note = repository.findById(id).orElse(null);
        if (note == null) {
            throw new ResourceNotFoundException("No account with this ID.");
        }

        return note;
    }

    public List<Note> getNotes(
            String sectionID, String courseID, String majorID,
            String semesterID, String schoolID, List<String> tags,
            String search, String sortType, String orderType) {

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
        }

        query.addCriteria(Criteria.where("tags").all(tags));

        query.addCriteria(Criteria.where("title").regex(search, "i")
                .orOperator(Criteria.where("description").regex(search, "i")));

        if (majorID != null) {
            query.addCriteria(Criteria.where("major").is(majorID));
        }

        if (semesterID != null) {
            query.addCriteria(Criteria.where("semester").is(semesterID));
        }

        if (schoolID != null) {
            query.addCriteria(Criteria.where("school").is(schoolID));
        }

        return template.find(query, Note.class);
    }

    public void updateNote(Note note) {
        repository.save(note);
    }

    public void deleteNote(Note note) {
        repository.delete(note);
        files.deleteFile(note.getFile());
    }
}
