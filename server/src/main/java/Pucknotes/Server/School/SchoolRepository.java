package Pucknotes.Server.School;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SchoolRepository extends MongoRepository<School, String> {
    Optional<School> findByName(String name);
    boolean existsByName(String name);
}