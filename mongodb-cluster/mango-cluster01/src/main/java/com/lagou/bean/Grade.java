package com.lagou.bean;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 */
@Document(collection = "grade")
@Data
public class Grade {

    private  Integer id;
    private  String gradeName;
}

