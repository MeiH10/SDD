package Pucknotes.Server.Major;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.School.School;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Document(collection = "majors")
public class MajorService {
    @Autowired
    private MajorRepository repository;

    @Autowired
    private MongoTemplate template;

    public List<Major> getMajors(
            String semesterID,
            String schoolID,
            String name,
            String sortType,
            String orderType) {

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

        if (semesterID != null) {
            query.addCriteria(Criteria.where("semester").is(semesterID));
        }

        if (schoolID != null) {
            query.addCriteria(Criteria.where("school").is(schoolID));
        }

        if (name != null) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }

        return template.find(query, Major.class);
    }

    public Major getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid major ID.");
        }

        Major major = repository.findById(id).orElse(null);
        if (major == null) {
            throw new ResourceNotFoundException("No major with this ID.");
        }

        return major;
    }

    public School getByCode(String code) {
        return repository.findByCode(code).orElse(null);
    }

    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    public boolean existsByCode(String code) {
        return repository.existsByCode(code);
    }
}
