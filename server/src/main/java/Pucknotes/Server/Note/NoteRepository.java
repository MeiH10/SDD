package Pucknotes.Server.Note;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * The NoteRepository interface extends the MongoRepository interface provided by Spring Data MongoDB.
 * This interface serves as a repository for managing Note entities, allowing CRUD (Create, Read, Update, 
 * Delete) operations on Note documents in the MongoDB database.
 */
public interface NoteRepository extends MongoRepository<Note, String> {

}
