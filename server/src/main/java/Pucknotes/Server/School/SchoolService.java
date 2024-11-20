package Pucknotes.Server.School;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Document(collection = "schools")
public class SchoolService {
    @Autowired
    private final SchoolRepository schoolRepository;

    public School getById(String id) {
        return schoolRepository.findById(id).orElse(null);
    }

    public School getByName(String name) {
        return schoolRepository.findByName(name).orElse(null);
    }

    public boolean existsById(String id) {
        return schoolRepository.existsById(id);
    }

    public boolean existsByName(String name) {
        return schoolRepository.existsByName(name);
    }
}
