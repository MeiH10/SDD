package Pucknotes.Server.Section;

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

    public Section getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid semester ID.");
        }

        Section section = repository.findById(id).orElse(null);
        if (section == null) {
            throw new ResourceNotFoundException("No semester with this ID.");
        }

        return section;
    }
}
