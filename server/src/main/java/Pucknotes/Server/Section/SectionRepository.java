package Pucknotes.Server.Section;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SectionRepository extends MongoRepository<Section, String> {
    List<Section> findByCourseIdAndNumber(String courseId, String number);
    List<Section> findByCourseId(String courseId);
    List<Section> findByNumber(String number);
    
    Optional<Section> findById(String id);
}