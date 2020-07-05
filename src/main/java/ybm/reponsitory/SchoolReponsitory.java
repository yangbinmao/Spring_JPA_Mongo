package ybm.reponsitory;

import ybm.document.School;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface SchoolReponsitory extends MongoRepository<School, ObjectId> {

    School findSchoolByName(String name);
}