package Pucknotes.Server.School;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Document(collection = "schools")
public class SchoolService {

}
