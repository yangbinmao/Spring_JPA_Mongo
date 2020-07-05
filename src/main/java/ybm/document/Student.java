package ybm.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="Student")
public class Student {
    @Id
    @Field("_id")
    private Long id;

    private String name;
    private Integer age;
    private School shool;
    private Date greatdata;

    public Student(String name, Integer age, School shool) {
        this.name = name;
        this.age = age;
        this.shool = shool;
        this.greatdata=new Date();
    }
}
