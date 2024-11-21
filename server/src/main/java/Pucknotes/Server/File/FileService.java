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

@Service
public class FileService {
    @Autowired
    private GridFsTemplate template;
    
    @Autowired
    private GridFsOperations operations;

    public ObjectId addFile(MultipartFile upload) throws IOException {
        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());

        return template.store(
            upload.getInputStream(),
            upload.getOriginalFilename(),
            upload.getContentType(),
            metadata
        );
    }

    public File downloadFile(String id) throws IOException {
        Query query = new Query(Criteria.where("_id").is(id));
        GridFSFile file = template.findOne(query);
        File result = new File();

        if (file != null && file.getMetadata() != null) {
            result.setFilename(file.getFilename());
            result.setFileType(MediaType.parseMediaType(file.getMetadata().get("_contentType").toString()));
            result.setFileSize(Long.parseLong(file.getMetadata().get("fileSize").toString()));

            result.setFile(IOUtils.toByteArray(operations.getResource(file).getInputStream()));
        }

        return result;
    }

}