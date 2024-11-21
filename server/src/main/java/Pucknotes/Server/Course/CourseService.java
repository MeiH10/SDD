package Pucknotes.Server.Course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Document(collection = "courses")
public class CourseService {
    @Autowired
    private CourseRepository repository;

    @Autowired
    private MongoTemplate template;

    public List<Course> getCourses(String majorID, String schoolID, String semesterID, String name, String sortType, String orderType) {
        Query query = new Query();

        Sort.Direction direction = "asc".equalsIgnoreCase(orderType)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        switch (sortType.toLowerCase()) {
            case "name":
                query.with(Sort.by(direction, "name"));
                break;
            case "semester":
                query.with(Sort.by(direction, "semester.year"));
                break;
        }

        if (majorID != null) {
            query.addCriteria(Criteria.where("major").is(majorID));
        }
        
        if (semesterID != null) {
            query.addCriteria(Criteria.where("semester").is(semesterID));
        }

        if (schoolID != null) {
            query.addCriteria(Criteria.where("school").is(schoolID));
        }

        if (name != null) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }

        return template.find(query, Course.class);
    }

    public Course getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid course ID.");
        }

        Course section = repository.findById(id).orElse(null);
        if (section == null) {
            throw new ResourceNotFoundException("No course with this ID.");
        }

        return section;
    }

    public Course getByCode(String code) {
        return repository.findByCode(code).orElse(null);
    }

    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    public boolean existsByCode(String code) {
        return repository.existsByCode(code);
    }
}
