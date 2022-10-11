package com.financia.exchange.test.mongodb.service;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 */
@Document(collection = "student")
@Data
public class Student {

    private String id;
    private String name;
    private String sex;
    private Integer gradeId;
    private Integer teacherId;
    @DBRef
    private  Grade grade;
    @DBRef
    private  Teacher teacher;

}

