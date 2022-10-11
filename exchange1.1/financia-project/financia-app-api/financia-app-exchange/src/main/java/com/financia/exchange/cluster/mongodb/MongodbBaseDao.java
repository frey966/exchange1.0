package com.financia.exchange.cluster.mongodb;


import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface MongodbBaseDao<T,ID> {

    /**
     * 增
     */
    boolean save(T entity);
    boolean saveList(List<T> list);
    /**
     * 删
     */
    boolean removeById(Serializable id);

    /**
     * 改,都是通过id
     * @param keys 要修改的键名
     * @param values 要修改的键值
     * 相当于mysql中  update key1,key2,key3 set value1,value2,value3 where id=#{id} from collectionName
     */
    boolean updateById(String[] keys, Object[] values, Serializable id);

    /**
     * 查
     * @finById 就是根据id查询单条记录
     * @findByMap 相当于条件查询,查询出符合条件的所有内容
     * @findAll 查询表中所有记录
     * @fields 表示需要查询的字段---参考mysql中 select key1,key2,key3 from collectionName
     */
    T findById(Serializable id);
    T findById(Serializable id, String[] fields);

    /**
     * 查询
     * @return
     */
    List<T> findAll();
    List<T> findAll(Query query);

    /**
     * 通过条件查询,查询分页结果 单表查询
     */
    Pagination<T> getPage(Long currentPage, Query query) ;

    /**
     *  多表关联分页排序查询
     * @param currentPage 当前页
     * @param lookupOperations 关联表集合
     *                         Aggregation.lookup("关联表名称","主表关联字段","关联表字段","结果名称")
     * @param criteria  主表筛选条件
                        Criteria.where("字段").is("值")
     * @param direction 排序规则
     *                  Direction.DESC  Direction.ASC
     * @param sortFields 排序字段
     * @return 指定对象集合
     */
    Pagination<T> getMultiSetPage(Long currentPage, List<AggregationOperation> lookupOperations, Criteria criteria, Sort.Direction direction, String... sortFields);

    /**
     *  多表关联分页查询
     * @param currentPage 当前页
     * @param lookupOperations 关联表集合
     *                         Aggregation.lookup("关联表名称","主表关联字段","关联表字段","结果名称")
     * @param criteria  主表筛选条件
                        Criteria.where("字段").is("值")
     * @return 指定对象集合
     */
    Pagination<T> getMultiSetPage(Long currentPage, List<AggregationOperation> lookupOperations, Criteria criteria);

    /**
     *  多表关联分页排序查询
     * @param currentPage 当前页
     * @param lookupOperations 关联表集合
     *                         Aggregation.lookup("关联表名称","主表关联字段","关联表字段","结果名称")
     * @param criteria  主表筛选条件
                        Criteria.where("字段").is("值")
     * @param direction 排序规则
     *                  Direction.DESC  Direction.ASC
     * @param sortFields 排序字段
     * @return LIST<map>集合
     */
    Pagination<Map> getMultiSetMapPage(Long currentPage, List<AggregationOperation> lookupOperations, Criteria criteria, Sort.Direction direction, String... sortFields);

    /**
     *  多表关联分页排序查询
     * @param currentPage 当前页
     * @param lookupOperations 关联表集合
     *                         Aggregation.lookup("关联表名称","主表关联字段","关联表字段","结果名称")
     * @param criteria  主表筛选条件
                        Criteria.where("字段").is("值")
     * @return LIST<map>集合
     */
    Pagination<Map> getMultiSetMapPage(Long currentPage, List<AggregationOperation> lookupOperations, Criteria criteria);



}
