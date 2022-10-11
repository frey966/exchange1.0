package com.lagou;

import cn.hutool.json.JSONUtil;
import com.lagou.bean.Pagination;
import com.lagou.bean.Student;
import com.lagou.dao.StudentDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoTemplateTest {
    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void insert() {
        Student student = new Student();
        student.setId(UUID.randomUUID().toString());
        student.setName("插入测试");
        student.setSex("男");
        student.setTeacherId(1);
        student.setGradeId(1);
        studentDao.save(student);
    }
     @Test
    public void update() {
        String [] keys = {"name","sex"};
        String [] values = {"修改测试name","男"};
        studentDao.updateById(keys,values,"def9cacb-7572-4fc1-a1f0-11c9f99d46b8");
    }

    @Test
    public void removeById() {
        studentDao.removeById("e2c6218e-0acb-402b-b4f7-2659ebe5b745");
    }
    /**
     * 分页查询 - 单表 (测试 MongodbBaseDao 通用方法)
     */
    @Test
    public void findPage (){
        Query query = new Query() ;
        Pagination<Student> studentAndGrade = studentDao.getPage(1l,query);
        System.out.println("getPageSize:"+studentAndGrade.getPageSize());
        System.out.println("getTotalPage:"+studentAndGrade.getTotalPage());
        System.out.println(JSONUtil.toJsonStr(studentAndGrade));

    }

    /**
     * 分页查询 - 多表  (测试 MongodbBaseDao 通用方法)
     */
    @Test
    public void findMultiSetPage (){
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        //from:关联从表名,localField:主表关联字段,foreignField:从表关联的字段,as:查询结果名
        aggregationOperations.add(Aggregation.lookup("grade","gradeId","_id","grade"));
        aggregationOperations.add(Aggregation.lookup("teacher","teacherId","_id","teacher"));
        Criteria criteria = new Criteria();
        criteria.where("sex").is("男");
        Pagination<Student> studentAndGrade = studentDao.getMultiSetPage(1l,aggregationOperations,criteria);
        System.out.println("getPageSize:"+studentAndGrade.getPageSize());
        System.out.println("getTotalPage:"+studentAndGrade.getTotalPage());
        System.out.println(JSONUtil.toJsonStr(studentAndGrade));

    }

    /**
     * 分页查询 - 多表 - 排序  (测试 MongodbBaseDao 通用方法)
     */
    @Test
    public void findMultiSetPageSort (){
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        //from:关联从表名,localField:主表关联字段,foreignField:从表关联的字段,as:查询结果名
        aggregationOperations.add(Aggregation.lookup("grade","gradeId","_id","grade"));
        aggregationOperations.add(Aggregation.lookup("teacher","teacherId","_id","teacher"));
        Pagination<Student> studentAndGrade = studentDao.getMultiSetPage(1l,aggregationOperations,
                null, Sort.Direction.DESC,"gradeId");
        System.out.println("getPageSize:"+studentAndGrade.getPageSize());
        System.out.println("getTotalPage:"+studentAndGrade.getTotalPage());
        System.out.println(JSONUtil.toJsonStr(studentAndGrade));

    }

    /**
     * 分页查询 - 多表 (Map) (测试 MongodbBaseDao 通用方法)
     */
    @Test
    public void findMultiSetMapPage (){
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        aggregationOperations.add(Aggregation.lookup("grade","gradeId","_id","grade"));
        aggregationOperations.add(Aggregation.lookup("teacher","teacherId","_id","teacher"));
        Criteria criteria = new Criteria();
        criteria.where("sex").is("男");
        Pagination<Map> studentAndGrade = studentDao.getMultiSetMapPage(1l,aggregationOperations,criteria);
        System.out.println("getPageSize:"+studentAndGrade.getPageSize());
        System.out.println("getTotalPage:"+studentAndGrade.getTotalPage());
        System.out.println(JSONUtil.toJsonStr(studentAndGrade));

    }

    /**
     * 单表查询排序
     */
    @Test
    public void findAll (){
        Query query = new Query();
        query.limit(10);
        query.with(Sort.by(Sort.Direction.DESC,"gradeId"));
        List<Student> all = studentDao.findAll(query);
        System.out.println(JSONUtil.toJsonStr(all));
    }

    @Test
    public void test (){
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.lookup("grade", "gradeId", "_id", "grade"), Aggregation.sort(Sort.Direction.DESC, "gradeId"),Aggregation.limit(10));
        AggregationResults<Student> aggregate = mongoTemplate.aggregate(aggregation, Student.class, Student.class);
        List<Student> mappedResults = aggregate.getMappedResults();
        System.out.println(JSONUtil.toJsonStr(mappedResults));
    }
}
