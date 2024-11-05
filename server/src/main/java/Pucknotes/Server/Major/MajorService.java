package Pucknotes.Server.Major;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import Pucknotes.Server.Semester.Semester;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Document(collection = "majors")
public class MajorService {
    @Autowired
    private MajorRepository repository;

    public List<Major> getMajors(String school, String name, Semester semester, String sort, String order) {
        Sort sortOrder = Sort.unsorted();

        if ("name".equalsIgnoreCase(sort)) {
            sortOrder = "asc".equalsIgnoreCase(order)
                    ? Sort.by(Sort.Order.asc("name"))
                    : Sort.by(Sort.Order.desc("name"));
        } else if ("semester".equalsIgnoreCase(sort)) {
            sortOrder = "asc".equalsIgnoreCase(order)
                    ? Sort.by(Sort.Order.asc("semester.year"))
                    : Sort.by(Sort.Order.desc("semester.year"));
        }

        if (school != null && semester != null && name != null) {
            return repository.findBySchoolAndNameContaining(school, name, sortOrder);
        } else if (school != null && semester != null) {
            return repository.findBySemester(semester, sortOrder);
        } else if (school != null && name != null) {
            return repository.findBySchoolAndNameContaining(school, name, sortOrder);
        } else if (name != null) {
            return repository.findByNameContaining(name, sortOrder);
        } else if (school != null) {
            return repository.findBySchool(school, sortOrder);
        } else {
            return repository.findAll(sortOrder);
        }
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
}
