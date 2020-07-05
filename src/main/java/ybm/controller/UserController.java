package ybm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ybm.document.User;
import ybm.reponsitory.UserReponsitory;
import ybm.server.UserServer;

import java.util.List;

@Controller
@Slf4j
public class UserController {

    @Autowired
    UserReponsitory userReponsitory;


    @Autowired
    UserServer userServer;

    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping("findAll")
    public List<User> findAll(){
        List<User> all = userServer.findAll();
        for (User user : all) {
            log.info(user.toString());
        }
        return all;
    }

    @RequestMapping("/")
    public List<User> findAll1(){
        System.out.println(11111);
        List<User> all = userServer.findAll();
        for (User user : all) {
            log.info(user.toString());
        }
        return all;
    }

    @RequestMapping("/del")
    @ResponseBody
    public String del(){
        System.out.println(11111);
//         userReponsitory.deleteById("5f01ca61c2d67464336c2f75");
//        userServer.delUserById("5f01ca61c2d67464336c2f75");
//        userReponsitory.delete(new User("5f01ca61c2d67464336c2f76","增加的名字1", "admin1", "password1"));

        Query query = Query.query(Criteria.where("_is").is("5f01ca61c2d67464336c2f75"));

        mongoTemplate.remove(query, "user");

        return "ok";
    }
}
