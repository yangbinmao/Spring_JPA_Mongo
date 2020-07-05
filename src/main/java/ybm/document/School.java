package ybm.document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class School {
    @Id
    @Field("_id")
    @Indexed
    private ObjectId id;

    private String name;
    private String address;

    public School(String name, String address) {
        this.name = name;
        this.address = address;
    }


}
