package ybm.reponsitory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ybm.document.User;


public interface UserReponsitory extends MongoRepository<User,String> {

    /**
     * 根据name获取用户,并且排序
     * @param name
     * @param pageable
     * @return
     */
    public Page<User> findByNameLike(String name, Pageable pageable);
}
