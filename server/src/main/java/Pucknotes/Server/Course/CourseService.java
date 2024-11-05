package Pucknotes.Server.Course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Document(collection = "courses")
public class CourseService {
    @Autowired
    private CourseRepository repository;

    public Course getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid semester ID.");
        }

        Course section = repository.findById(id).orElse(null);
        if (section == null) {
            throw new ResourceNotFoundException("No semester with this ID.");
        }

        return section;
    }
}
