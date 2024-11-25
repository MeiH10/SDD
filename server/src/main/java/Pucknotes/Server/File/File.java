package Pucknotes.Server.File;

import org.springframework.http.MediaType;

import lombok.Getter;
import lombok.Setter;

/**
 * The File class represents a file with its associated attributes such 
 * as the filename, file type, file size, and the actual file content 
 * in byte array format. This class makes use of Lombok annotations 
 * to generate getters and setters for its fields automatically.
 */
@Getter
@Setter
public class File {

    /**
     * The name of the file. This field should hold the original filename
     * as it was provided by the user or the system. It is essential for
     * file identification and retrieval.
     */
    private String filename;

    /**
     * The media type of the file, which indicates the format of the 
     * file content (e.g., text/plain, image/jpeg). This information 
     * can be useful for clients that need to know how to process or 
     * display the file.
     */
    private MediaType fileType;

    /**
     * The size of the file in bytes. This field helps in determining 
     * if the file is too large or too small for specific operations 
     * or requirements. Size validation can prevent storage issues or 
     * performance degradation.
     */
    private long fileSize;

    /**
     * The content of the file represented as a byte array. This is the 
     * actual data that is stored in the file. Clients will typically 
     * need to read or write this data to perform file operations.
     */
    private byte[] file;
}
