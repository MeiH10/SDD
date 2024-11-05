package Pucknotes.Server.Course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Major.Major;
import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Document(collection = "courses")
public class CourseService {
    @Autowired
    private CourseRepository repository;

    public List<Course> getCourses(Major major, String majorCode, String code, String name, String sort, String order) {
        Sort sortOrder = Sort.unsorted();

        if ("name".equalsIgnoreCase(sort)) {
            sortOrder = "asc".equalsIgnoreCase(order) ?
                    Sort.by(Sort.Order.asc("name")) : Sort.by(Sort.Order.desc("name"));
        } else if ("major.code".equalsIgnoreCase(sort)) {
            sortOrder = "asc".equalsIgnoreCase(order) ?
                    Sort.by(Sort.Order.asc("major.code")) : Sort.by(Sort.Order.desc("major.code"));
        }

        if (majorCode != null && !majorCode.isEmpty()) {
            return repository.findByMajorCode(majorCode, sortOrder);
        } else if (major != null && code != null && name != null) {
            return repository.findByMajorAndCodeAndNameContaining(major, code, name, sortOrder);
        } else if (major != null && code != null) {
            return repository.findByMajorAndCode(major, code, sortOrder);
        } else if (major != null && name != null) {
            return repository.findByMajorAndNameContaining(major, name, sortOrder);
        } else if (code != null) {
            return repository.findByCode(code, sortOrder);
        } else if (major != null) {
            return repository.findByMajor(major, sortOrder);
        } else if (name != null) {
            return repository.findByNameContaining(name, sortOrder);
        } else {
            return repository.findAll(sortOrder);
        }
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
}
