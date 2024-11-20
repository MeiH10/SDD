package Pucknotes.Server.School;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Document(collection = "schools")
public class SchoolService {
    @Autowired
    private final SchoolRepository repository;

    @Autowired
    private MongoTemplate template;

    public School getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public School getByName(String name) {
        return repository.findByName(name).orElse(null);
    }

    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    public List<School> getSchool(
            String name,
            String semesterID,
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

        if (name != null) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }

        return template.find(query, School.class);
    }
}
