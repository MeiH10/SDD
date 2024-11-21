package Pucknotes.Server.Section;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SectionRepository extends MongoRepository<Section, String> {
    boolean existsByCode(String number);
    Section getByNumber(String number);
}