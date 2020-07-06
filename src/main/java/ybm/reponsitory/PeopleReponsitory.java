package ybm.reponsitory;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ybm.document.People;

public interface PeopleReponsitory extends MongoRepository<People, ObjectId> {
}
