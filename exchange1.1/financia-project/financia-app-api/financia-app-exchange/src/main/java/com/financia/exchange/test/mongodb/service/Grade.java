package com.financia.exchange.test.mongodb.service;

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

