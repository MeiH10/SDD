package Pucknotes.Server.Course;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<Course, String> {
    Optional<Course> findByCode(String code);
    boolean existsByCode(String code);
}