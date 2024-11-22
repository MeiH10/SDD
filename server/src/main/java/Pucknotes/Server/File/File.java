package Pucknotes.Server.File;

import org.springframework.http.MediaType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class File {
    private String filename;
    private MediaType fileType;
    private long fileSize;
    private byte[] file;
}
