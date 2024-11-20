package Pucknotes.Server.Report;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Document(collection = "comments")
public class Report {
    @Id
    private String id;

    @Setter
    private String title;

    @Setter
    private String description;
}
