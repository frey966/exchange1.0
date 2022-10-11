package com.lagou.repository;

import com.lagou.bean.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentRepository extends MongoRepository<Student,String> {

    List<Student> findByNameEquals(String name);
    List<Student> findByNameAndSex(String name,String expectSalary);
}
