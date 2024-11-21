package Pucknotes.Server.Major;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import Pucknotes.Server.School.School;

public interface MajorRepository extends MongoRepository<Major, String> {
    Optional<School> findByCode(String code);
    boolean existsByCode(String code);
}