package com.financia.exchange.test.mongodb.service;


import com.financia.exchange.cluster.mongodb.MongodbBaseDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 15579
 * 2019/6/13 14:33
 * 文件说明：
 */
@Component("studentDao")
public class StudentDaoImpl extends MongodbBaseDaoImpl<Student,String> implements StudentDao {

    @Autowired
    private MongoTemplate mongoTemplate;

}
