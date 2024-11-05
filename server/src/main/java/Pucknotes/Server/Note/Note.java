package Pucknotes.Server.Note;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.Binary;

@Document(collection = "notes")
public class Note {

    @Id
    private String id;
    private String title;
    private Binary image;

    // No-argument constructor
    public Note() {
    }

    // Constructor with all fields
    public Note(String id, String title, Binary image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    // Constructor with title and image only (without id)
    public Note(String title, Binary image) {
        this.title = title;
        this.image = image;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Binary getImage() {
        return image;
    }

    public void setImage(Binary image) {
        this.image = image;
    }
}