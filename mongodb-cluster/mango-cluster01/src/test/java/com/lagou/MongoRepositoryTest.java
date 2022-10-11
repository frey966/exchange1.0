package com.lagou;

import cn.hutool.json.JSONUtil;
import com.lagou.bean.Student;
import com.lagou.repository.StudentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    /**
     *
     */
    @Test
    public void findAll (){
        List<Student> all = studentRepository.findAll();
        System.out.println(JSONUtil.toJsonStr(all));
    }

    @Test
    public void findByName (){
        List<Student> all = studentRepository.findByNameEquals("测试80");
        System.out.println(JSONUtil.toJsonStr(all));
    }

    @Test
    public void findByNameAndSex (){
        List<Student> all = studentRepository.findByNameAndSex("测试80","男");
        System.out.println(JSONUtil.toJsonStr(all));
    }

    /**
     * 分页查询
     */
    @Test
    public void findByExample (){
        //分页查询对象
        PageRequest page = PageRequest.of(2, 10);
        Student student = new Student();
        student.setSex("男");
        Example<Student> of = Example.of(student);
        Page<Student> all = studentRepository.findAll(of, page);
        System.out.println(JSONUtil.toJsonStr(all));
    }

    @Test
    public void findByExampleOne (){
        Student student = new Student();
        student.setId("41a60960-8d47-493c-91da-d50c055b1a89");
        Example<Student> of = Example.of(student);
        Optional<Student> one = studentRepository.findOne(of);
        Student student1 = one.get();
        System.out.println(JSONUtil.toJsonStr(student1));
    }
}
