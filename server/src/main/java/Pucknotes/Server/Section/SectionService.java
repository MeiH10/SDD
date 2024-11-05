package Pucknotes.Server.Section;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Document(collection = "sections")
public class SectionService {
    @Autowired
    private SectionRepository repository;

    public List<Section> getSections(String courseId, String number) {
        if (courseId != null && number != null) {
            return repository.findByCourseIdAndNumber(courseId, number);
        } else if (courseId != null) {
            return repository.findByCourseId(courseId);
        } else if (number != null) {
            return repository.findByNumber(number);
        } else {
            return repository.findAll();
        }
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
