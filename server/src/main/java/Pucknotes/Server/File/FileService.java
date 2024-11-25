package Pucknotes.Server.File;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * FileService is a service class that manages file operations,
 * including uploading, downloading, and deleting files stored in MongoDB GridFS.
 */
@Service
public class FileService {
    
    @Autowired
    private GridFsTemplate template;
    
    @Autowired
    private GridFsOperations operations;

    /**
     * Adds a file to the MongoDB GridFS.
     *
     * @param upload the MultipartFile object containing the file to be uploaded
     * @return the ObjectId of the stored file
     * @throws IOException if an error occurs while reading the file input stream or storing the file
     * 
     * This method creates a metadata object that includes the file size
     * and stores the file using the GridFsTemplate. The object's ObjectId is returned.
     */
    public ObjectId addFile(MultipartFile upload) throws IOException {
        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());

        // The input stream is obtained from the MultipartFile and stored in GridFS.
        return template.store(
            upload.getInputStream(),
            upload.getOriginalFilename(),
            upload.getContentType(),
            metadata
        );
    }

    /**
     * Downloads a file from the MongoDB GridFS based on the provided ID.
     *
     * @param id the ID of the file to be downloaded
     * @return a File object containing the downloaded file information
     * @throws IOException if an error occurs while reading the file input stream
     * 
     * This method retrieves the file from GridFS using its ID, populating a 
     * File object that includes the filename, content type, file size, and file bytes.
     */
    public File downloadFile(String id) throws IOException {
        Query query = new Query(Criteria.where("_id").is(id));
        GridFSFile file = template.findOne(query);
        File result = new File();

        // Checking if the file was found in GridFS and has metadata.
        if (file != null && file.getMetadata() != null) {
            result.setFilename(file.getFilename());
            result.setFileType(MediaType.parseMediaType(file.getMetadata().get("_contentType").toString()));
            result.setFileSize(Long.parseLong(file.getMetadata().get("fileSize").toString()));

            // Converting the input stream to byte array for easy access.
            result.setFile(IOUtils.toByteArray(operations.getResource(file).getInputStream()));
        }

        return result;
    }

    /**
     * Deletes a file from the MongoDB GridFS based on the provided ID.
     *
     * @param id the ID of the file to be deleted
     * 
     * This method creates a query based on the file ID and deletes the file 
     * from GridFS. It does not provide feedback if the file was not found.
     */
    public void deleteFile(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        template.delete(query);
        // Note: Additional error handling could be implemented here.
    }
}
