package Pucknotes.Server.Major;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import Pucknotes.Server.Semester.Semester;

public interface MajorRepository extends MongoRepository<Major, String> {
    List<Major> findBySchoolAndNameContaining(String school, String name, Sort sort);
    List<Major> findBySemester(Semester semester, Sort sort);
    List<Major> findBySchool(String school, Sort sort);
    List<Major> findByNameContaining(String name, Sort sort);
    List<Major> findAll(Sort sort);
}