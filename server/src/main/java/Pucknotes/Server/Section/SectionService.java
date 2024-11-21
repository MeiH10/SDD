package Pucknotes.Server.Section;

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
@Document(collection = "sections")
public class SectionService {
    @Autowired
    private SectionRepository repository;

    @Autowired
    private MongoTemplate template;
    
    public List<Section> getSections(String courseId, String sortType, String orderType) {
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

        if (courseId != null) {
            query.addCriteria(Criteria.where("course").is(courseId));
        }

        return template.find(query, Section.class);
    }

    public Section getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid section ID.");
        }

        Section section = repository.findById(id).orElse(null);
        if (section == null) {
            throw new ResourceNotFoundException("No section with this ID.");
        }

        return section;
    }
}
