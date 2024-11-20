package Pucknotes.Server.Semester;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SemesterRepository extends MongoRepository<Semester, String> {
    List<Semester> findBySeasonAndYear(String season, int year, Sort sort);
    List<Semester> findBySeason(String season, Sort sort);
    List<Semester> findByYear(int year, Sort sort);
    List<Semester> findAll(Sort sort);
    
    Optional<Semester> findById(String id);

    Optional<Semester> findByName(String name);
    boolean existsByName(String name);
}