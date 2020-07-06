package mongoTemplate;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ObjectUtils;
import ybm.MySpringBootApplication;
import ybm.document.Student;
import ybm.document.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MySpringBootApplication.class)
@Slf4j
public class mongoTemplateTest {

	@Autowired
	MongoTemplate mongoTemplate;
 
	/**
	 * 一、save方法
	 * 遍历插入
	 * 若新增数据的主键已经存在，则会对当前已经存在的数据进行修改操作
	 * */
	@Test
	public void saveTest() {

		User user = new User();
		user.setName("wwze01");
		user.setUsername("wwze01");
		user.setPassword("123456");
//		System.out.println(mongoTemplate);
		log.info("打印："+mongoTemplate);
		mongoTemplate.save(user);	
	}


	/**
	 * save方法可以用来更新。
	 * 但是更新一定要是全字段，不然其他的属性就会被设置为null
	 */
	@Test
	public void saveTest2() {
		List<User> users = mongoTemplate.find(new Query().addCriteria(Criteria.where("username").is("wwze01")), User.class);
		User user = users.get(0);
		log.info(user.toString());
		user.setName("ybmtest");
		User user1 = new User();
		user1.setId(user.getId());
		user1.setName("uuuu");
		User save = mongoTemplate.save(user1);

		log.info(save.toString());
	}
 
	/**
	 * 二、insert方法
	 * 一次性插入一整个列表，而不用进行遍历操作，效率相对较高
	 * 若新增数据的主键已经存在，则会抛 org.springframework.dao.DuplicateKeyException 异常提示主键重复，不保存当前数据。
	 * */
	@Test
	public void insertTest() {
		User user = new User();
		user.setName("wwze02");
		user.setUsername("wwze02");
		user.setPassword("123");
		System.out.println(mongoTemplate);

	}
	
	/**
	 * 三、批量插入
	 * 
	 * */
	@Test
	public void insertAllTest() {
		ArrayList<User> manyUser = new ArrayList<>();
		User user1 = new User();
		user1.setName("wwze03");
		user1.setUsername("wwze03");
		user1.setPassword("1234");
		manyUser.add(user1);
		User user2 = new User();
		user2.setName("wwze04");
		user2.setUsername("wwze04");
		user2.setPassword("12345");
		manyUser.add(user2);

		Collection<User> users = mongoTemplate.insertAll(manyUser);
	}
	
	/**
	 * 四、插入嵌套文档
	 * 这种复杂的数据结构如果直接使用javabean是比较麻烦的，这种比较复杂的数据结构，
	 * 只需要拿到json字符串，并将其转换成json对象，即可直接插入到mongodb中
	 * */
	@Test
	public void insertNestTest(){ 
		String classStr = "{'classId':'1','Students':[{'studentId':'1','name':'zhangsan'}]}";
		JSONObject parseObject = JSONObject.parseObject(classStr);
		mongoTemplate.insert(parseObject,"class");//class为collection
	}
	
	/**
	 * 五、更新数据
	 * upsert和updateFirst、updateMulti
	 *
	 * 更改的API大概有这些
	 * updateMulti 如果根据查询条件找到对应的多条记录是，全部更新
	 * updateFirst 更改符合条件的第一个
	 * upsert 顾名思义 update+insert 如果根据条件没有对应的数据,则执行插入
	 * findAndModify 查询然后更新
	 *
	 * */
	@Test
	public void update() {
		Query query = new Query();
		
		//Criteria.where(属性名).后可跟lt lte gt gte等
		query.addCriteria(Criteria.where("classId").is("1"));

		Update update = new Update();
		update = Update.update("teacher","WANGZE");
		mongoTemplate.upsert(query, update, "fuzui");
		
		/*
		 * upsert方法时如果query条件没有筛选出对应的数据，那么upsert会插入一条新的数据，而update什么都不会做
		 * 等同于关系型数据库中的merge和update。
		mongoTemplate.updateFirst(query, update, "class");
		mongoTemplate.updateMulti(query, update, "class");
		*/	
	}
	
	/**
	 * 六、添加嵌套文档中的数据
	 * addToSet方法：如果要插入的数据已经存在，则不进行任何操作；
	 * push方法：即使要插入的数据已存在，但仍要插入一条一样的数据。
	 * */
	@Test
	public void addNestTest() {
		Query query = Query.query(Criteria.where("classId").is("1"));
		User user = new User("wwze05","wwze05","123");
		Update update = new Update();
		
		update.push("Students",user);
		//update.addToSet("Students", user);
		mongoTemplate.upsert(query, update, "fuzui");
	}
	
	/**
	 * 七、修改
	 * mongoTemplate.updateMulti ,修改所有符合条件的
	 * mongoTemplate.upset
	 * 只更新第一条
	 * */
	@Test
	public void updateNestTest() {
		Query query = new Query().addCriteria(Criteria.where("age").is(12));
		Update update = new Update();
		update.set("name","哈哈哈");
		UpdateResult result = mongoTemplate.updateMulti(query, update, Student.class);

		System.out.println("result = " + result);
	}
	
	/**
	 * 八、删除内嵌文档中的数据
	 * 只删除第一条
	 * 不能彻底删除，会把符合条件的数据修改为空；
	 * 如需彻底删除，需使用pull方法，pull方法中的实体对象数据应与被删除的完全一样。
	 * */
	@Test
	public void deleteNestTest() {
		Query query = Query.query(Criteria.where("classId").is("1").and("Students.name").is("wwze05"));
		Update update = new Update();
		update.unset("Students.$");
		mongoTemplate.upsert(query, update, "fuzui");
	}
	
	/**
	 * 九、删除
	 * remove方法
	 * */
	@Test
	public void deleteTest() {
		Query query = Query.query(Criteria.where("classId").is("1"));
		mongoTemplate.remove(query,"fuzui");
	}
 
	/**
	 * 十、查询全部
	 * */
	@Test
	public void queryAll(){
		List<User> userList = mongoTemplate.find(new Query(),User.class);
		log.info("------------------------find------------------------");
		for (User user : userList) {
			log.info(user.toString());
		}
		log.info("------------------------findAll---------------------");
		List<User> userListAll = mongoTemplate.findAll(User.class);
		for (User user : userListAll) {
			log.info(user.toString());
		}
	}

	/**
	 * 条件查询（单条件查询）
	 * 查询User里面名是wwze03的
	 */
	@Test
	public void findis(){
		Query query = new Query();

		query.addCriteria(Criteria.where("name").is("wwze03"));
		List<User> userList = mongoTemplate.find(query,User.class);

		for (User user : userList) {
			log.info(user.toString());
		}


	}

	/**
	 *
	 * 条件查询（多条件查询）
	 * 	查询User里面名是wwze03的和wwze02
	 */
	@Test
	public void findIsAnd(){
		Query query = new Query();

		query.addCriteria(Criteria.where("name").is("wwze03").and("username").is("wwze02"));
		List<User> userList = mongoTemplate.find(query,User.class);
		log.info("{}",ObjectUtils.isEmpty(userList));
		for (User user : userList) {
			log.info(user.toString());
		}


	}
	/**
	 *
	 * 条件查询（范围查询之in）
	 * 	查询User里面名是wwze03的和wwze02
	 */
	@Test
	public void findAnd(){
		Query query = new Query();
		String[] strings = new String[]{"wwze03","wwze04"};
		query.addCriteria(Criteria.where("name").in(strings));
		List<User> userList = mongoTemplate.find(query,User.class);
		for (User user : userList) {
			log.info(user.toString());
		}
	}

	/**
	 *
	 * 条件查询（分页查找）
	 *
	 */
	@Test
	public void findPage(){
		int pageNo=2;
		int pageSize=2;
		Query query = new Query();

//		设置分页
		//设置查询当从那一条开始
		query.skip((pageNo-1)*pageSize);
		//设置查询需要多少条
		query.limit(pageSize);

//		设置查询条件
		query.addCriteria(Criteria.where("baseid").gte(0));
		List<User> userList = mongoTemplate.find(query,User.class);
		for (User user : userList) {
			log.info(user.toString());
		}
	}

	@Test
	public void findPage2(){
		Pageable page= PageRequest.of(1,2, Sort.Direction.ASC,"baseid");
		Query query1 = new Query();
		Query with = query1.with(page);
		List<User> users = mongoTemplate.find(query1, User.class);
		for (User user : users) {
			System.out.println("user = " + user);
		}
		
	}


	/**
	 * 	得到某个实体类在mongo库中的表名
	 */
	@Test
	public void getCollectionName(){

		String collectionName = mongoTemplate.getCollectionName(User.class);
		System.out.println("collectionName = " + collectionName);

	}
}
