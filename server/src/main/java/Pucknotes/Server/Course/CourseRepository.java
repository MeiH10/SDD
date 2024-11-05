package Pucknotes.Server.Course;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<Course, String> {
    List<Course> findByMajor(String majorID, Sort sort);
    List<Course> findByCode(String code, Sort sort);
    List<Course> findByNameContaining(String name, Sort sort);
    List<Course> findByMajorCode(String majorCode, Sort sort);
    List<Course> findByMajorAndCode(String majorID, String code, Sort sort);
    List<Course> findByMajorAndNameContaining(String majorID, String name, Sort sort);
    List<Course> findByMajorAndCodeAndNameContaining(String majorID, String code, String name, Sort sort);
    List<Course> findAll(Sort sort);

    Optional<Course> findById(String id);
}