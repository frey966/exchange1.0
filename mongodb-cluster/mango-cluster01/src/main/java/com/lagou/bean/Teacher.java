package com.lagou.bean;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 */
@Document(collection = "teacher")
@Data
public class Teacher {

    private  Integer id;
    private  String teacherName;
    private  String sex;
}

