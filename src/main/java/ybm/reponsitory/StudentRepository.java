package ybm.reponsitory;

import ybm.document.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, Long> {

    Student findByName(String name);

}
