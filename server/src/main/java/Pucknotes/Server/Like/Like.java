package Pucknotes.Server.Like;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import com.mongodb.lang.NonNull;

@Document(collection = "likes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Like {

    @Id
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    @NonNull
    private String user;

    @NonNull
    private String type;

    @Field(targetType = FieldType.OBJECT_ID)
    @NonNull
    private String item;
}