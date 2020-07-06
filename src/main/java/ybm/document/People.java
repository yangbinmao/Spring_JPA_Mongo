package ybm.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class People {
    @Id
    @Field("_id")
    ObjectId id;
    String name;
    int age;

    public People(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
