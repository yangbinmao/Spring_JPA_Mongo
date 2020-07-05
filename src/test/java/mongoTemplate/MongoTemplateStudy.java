package mongoTemplate;

import ybm.MySpringBootApplication;
import ybm.document.School;
import ybm.document.Student;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MySpringBootApplication.class)
public class MongoTemplateStudy {



    @Test
    public void first(){
        System.out.println("第一次测试");
    }

    @Autowired
    MongoTemplate template;

    @Test
    public void two(){
        School school = new School(new ObjectId("_id"),"第一中学","旁边小区");
        Student student = new Student("杨彬茂",24,school);
//        template.save(school);
//        template.insert(school);
        template.insert(new Student("杨彬茂",24,school));
//        添加和修改（和jpa一样）
        template.save(new School(new ObjectId("_id"),"第二中学","旁边小区"));
        template.save(new School("第三中学","旁边小区"));
    }

    @Test
    public void three(){

        template.save(new School("第4中学","旁边小区"));
        template.insert(new School("第5中学","旁边小区"));
    }

}
