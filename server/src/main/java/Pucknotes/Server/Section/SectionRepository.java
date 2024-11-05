package Pucknotes.Server.Section;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SectionRepository extends MongoRepository<Section, String> {
    Optional<Section> findById(String id);
}