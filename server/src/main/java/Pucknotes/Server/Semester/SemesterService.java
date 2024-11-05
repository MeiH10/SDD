package Pucknotes.Server.Semester;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import Pucknotes.Server.Response.Types.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Document(collection = "semesters")
public class SemesterService {
    @Autowired
    private SemesterRepository repository;

    public List<Semester> getSemesters(Integer year, String season, String sort) {
        Sort order = Sort.by("newest".equalsIgnoreCase(sort)
                ? Sort.Order.desc("year")
                : Sort.Order.asc("year"));

        if (year == -1 && season.equals("any")) {
            return repository.findAll();
        } else if (season != null && !season.equals("any") && year == -1) {
            return repository.findBySeason(season, order);
        } else if (season.equals("any") && year != -1) {
            return repository.findByYear(year, order);
        } else {
            return repository.findBySeasonAndYear(season, year, order);
        }
    }

    public Semester getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid semester ID.");
        }

        Semester account = repository.findById(id).orElse(null);
        if (account == null) {
            throw new ResourceNotFoundException("No semester with this ID.");
        }

        return account;
    }
}
