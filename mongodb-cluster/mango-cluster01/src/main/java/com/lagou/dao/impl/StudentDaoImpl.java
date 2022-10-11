package com.lagou.dao.impl;

import cn.hutool.json.JSONUtil;
import com.lagou.bean.Grade;
import com.lagou.bean.Student;
import com.lagou.bean.Teacher;
import com.lagou.dao.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

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
