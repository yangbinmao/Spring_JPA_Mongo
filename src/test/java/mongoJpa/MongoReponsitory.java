package mongoJpa;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import ybm.MySpringBootApplication;
import ybm.document.User;
import ybm.reponsitory.UserReponsitory;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MySpringBootApplication.class)
@Slf4j
@Rollback(value = false)
public class MongoReponsitory {

    @Autowired
    UserReponsitory userReponsitory;

    //---------------------------------增加----------------------------------------

    /**
     * 添加用户
     * userReponsitory.save或者userReponsitory.insert
     */
    @Test
    public void  addUser(){
        //save和insert的区别
        //save在增加的时候如果id重复了，他就是修改，save在新增的时候会遍历一次document，因为会看有没有相同的id
        //insert在增加的时候如果id重复了，他就会抛出异常，insert在新增的时候自己确定索引，有就抛出异常
        //save测试
        User userSave = userReponsitory.save(new User("增加的名字", "admin", "password"));
        //insert测试
        User userInsert = userReponsitory.insert(new User("增加的名字1", "admin1", "password1"));
        System.out.println(userSave+"======="+userInsert);
    }

    //---------------------------------修改----------------------------------------
    @Test
    public void updateUser() throws Exception {
        //先得到要修改的对象
        Optional<User> userOpt = userReponsitory.findById("5f01ca61c2d67464336c2f75");

        if ( !userOpt.isPresent()){
            System.out.println("没有查到User对象");
            throw new Exception("没有查询到对象");
        }
        User user = userOpt.get();
        System.out.println("user = " + user);
        user.setName("修改的名字");
        //还是使用save方法进行修改
//        User save = userReponsitory.save(user);
//        System.out.println("save = " + save);
    }



    //---------------------------------删除----------------------------------------
    @Test
    public void delUser(){
        //首先获取要删除
//        userReponsitory.deleteById("5f01ca61c2d67464336c2f75");
        userReponsitory.delete(new User("5f01ca61c2d67464336c2f76","增加的名字1", "admin1", "password1"));
//        userReponsitory.deleteAll();
    }




    @Test
    public void findAll(){
        List<User> all = userReponsitory.findAll();
        for (User user : all) {
            log.info(user.toString());
        }

    }

    /**
     * 在UserrReponsitory内使用jpa构造方法进行分页查询
     */
    @Test
    public void  wait1(){
        int page=1;
        int size=1;
        Pageable pageable= PageRequest.of(page-1,size);
        Page<User> page1 = userReponsitory.findByNameLike("ww", pageable);
        long totalElements = page1.getTotalElements();
        List<User> content = page1.getContent();
        System.out.println(page1.toString());
        for (User user : content) {
            log.info(user.toString());
        }
    }
}
