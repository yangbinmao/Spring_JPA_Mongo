package ybm.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ybm.document.User;
import ybm.reponsitory.UserReponsitory;

import java.util.List;

@Service
@Transactional
public class UserServer {

    @Autowired
    UserReponsitory userReponsitory;

    public List<User> findAll(){
        return  userReponsitory.findAll();
    }


    public void delUserById(String id){
         userReponsitory.deleteById(id);
    }
}
