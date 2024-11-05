package Pucknotes.Server.Course;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import Pucknotes.Server.Major.Major;

public interface CourseRepository extends MongoRepository<Course, String> {
    List<Course> findByMajor(Major major, Sort sort);
    List<Course> findByCode(String code, Sort sort);
    List<Course> findByNameContaining(String name, Sort sort);
    List<Course> findByMajorCode(String majorCode, Sort sort);
    List<Course> findByMajorAndCode(Major major, String code, Sort sort);
    List<Course> findByMajorAndNameContaining(Major major, String name, Sort sort);
    List<Course> findByMajorAndCodeAndNameContaining(Major major, String code, String name, Sort sort);
    List<Course> findAll(Sort sort);

    Optional<Course> findById(String id);
}