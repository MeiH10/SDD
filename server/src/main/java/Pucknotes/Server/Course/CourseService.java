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
            System.out.println(1);
            return repository.findByMajorCode(majorCode, sortOrder);
        } else if (major != null && code != null && !name.isEmpty()) {
            System.out.println(2);
            return repository.findByMajorAndCodeAndNameContaining(major.getId(), code, name, sortOrder);
        } else if (major != null && !code.isEmpty()) {
            System.out.println(3);
            return repository.findByMajorAndCode(major.getId(), code, sortOrder);
        } else if (major != null && !name.isEmpty()) {
            System.out.println(4);
            return repository.findByMajorAndNameContaining(major.getId(), name, sortOrder);
        } else if (!code.isEmpty()) {
            System.out.println(5);
            return repository.findByCode(code, sortOrder);
        } else if (major != null) {
            System.out.println(6);
            return repository.findByMajor(major.getId(), sortOrder);
        } else if (!name.isEmpty()) {
            System.out.println(7);
            return repository.findByNameContaining(name, sortOrder);
        } else {
            System.out.println(8);
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
