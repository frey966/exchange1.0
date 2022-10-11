/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.financia.exchange.test.mongodb.controller;
import com.financia.exchange.cluster.mongodb.Pagination;
import com.financia.exchange.test.mongodb.service.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 */
@RestController
public class MongoController {

    @Autowired
    private StudentDao studentDao;

    /**
     * test
     */
    @GetMapping("mongo1")
    public Pagination test(Long pageIndex){
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        aggregationOperations.add(Aggregation.lookup("grade","gradeId","_id","grade"));
        aggregationOperations.add(Aggregation.lookup("teacher","teacherId","_id","teacher"));
        Pagination<Map> multiSetMapPage = studentDao.getMultiSetMapPage(pageIndex, aggregationOperations, null);
        return multiSetMapPage;
    }

}
