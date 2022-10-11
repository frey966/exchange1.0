package com.lagou.dao.impl;

import com.lagou.bean.Pagination;
import com.lagou.dao.MongodbBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

public abstract class MongodbBaseDaoImpl<T,ID> implements MongodbBaseDao<T,ID> {
    @Autowired
    private MongoTemplate mongoTemplate;
    //每页显示五条
    protected static final int PAGE_SIZE = 8;

    /**
     * 通过条件查询,查询分页结果
     */
    public Pagination<T> getPage(Long currentPage, Query query) {
        //获取总条数
        Long totalCount = this.mongoTemplate.count(query, getTClass());
        //总页数
        Long totalPage = (totalCount/PAGE_SIZE);

        Long skip = (currentPage-1)*PAGE_SIZE;

        Pagination<T> page = new Pagination<T>(currentPage, totalPage,totalCount);
        query.skip(skip);// skip相当于从那条记录开始
        query.limit(PAGE_SIZE);// 从skip开始,取多少条记录
        List<T> datas = this.mongoTemplate.find(query,getTClass());
        page.build(datas);//获取数据
        return page;
    }


    /**
     * 多集合关联通过条件查询,查询分页结果
     */
    public Pagination<T> getMultiSetPage(Long currentPage, List<AggregationOperation> operations, Criteria criteria) {
        return getMultiSetPage(currentPage,operations, criteria,null,null);
    }

    /**
     * 多集合关联通过条件查询,查询分页结果(排序)
     */
    public Pagination<T> getMultiSetPage(Long currentPage, List<AggregationOperation> operations, Criteria criteria, Sort.Direction direction, String ... sortFields) {
        long totalCount = getTotalCount(criteria);
        //总页数
        long totalPage = (totalCount/PAGE_SIZE);

        long skipw = (currentPage-1)*PAGE_SIZE;

        Pagination<T> page = new Pagination(currentPage, totalPage, totalCount);
        Aggregation aggregation = aggregationBuild(operations, criteria, direction, skipw, sortFields);
        AggregationResults<T> aggregate = mongoTemplate.aggregate(aggregation,getTClass(), getTClass());
        List<T> datas = aggregate.getMappedResults();
        page.build(datas);//获取数据
        return page;
    }
    /**
     * 多集合关联通过条件查询,查询分页结果list<map>
     */
    public Pagination<Map> getMultiSetMapPage(Long currentPage, List<AggregationOperation> operations, Criteria criteria) {
        return getMultiSetMapPage(currentPage,operations,criteria,null,null);
    }
    /**
     * 多集合关联通过条件查询,查询分页结果list<map>(排序)
     */
    public Pagination<Map> getMultiSetMapPage(Long currentPage, List<AggregationOperation> operations, Criteria criteria, Sort.Direction direction, String ... sortFields) {

        long totalCount = getTotalCount(criteria);
        //总页数
        Long totalPage = (totalCount/PAGE_SIZE);
        Long skipw = (currentPage-1)*PAGE_SIZE;
        Pagination<Map> page = new Pagination<Map>(currentPage, totalPage, totalCount);
        Aggregation aggregation = aggregationBuild(operations, criteria, direction, skipw, sortFields);
        List<Map> datas = mongoTemplate.aggregate(aggregation,getTClass(), Map.class).getMappedResults();
        page.build(datas);//获取数据
        return page;
    }

    /**
     * 构建 aggregation
     * @param operations
     * @param criteria
     * @param direction
     * @param skipw
     * @param sortFields
     * @return
     */
    private Aggregation aggregationBuild(List<AggregationOperation> operations, Criteria criteria, Sort.Direction direction, Long skipw, String[] sortFields) {
        if (criteria != null) {
            AggregationOperation match = Aggregation.match(criteria);
            operations.add(match);
        }
        SkipOperation skip = Aggregation.skip(skipw); // 分页
        LimitOperation limit = Aggregation.limit(PAGE_SIZE); // 分页
        if (direction != null) {
            SortOperation sort = Aggregation.sort(direction, sortFields);
            operations.add(sort);
        }
        operations.add(skip);
        operations.add(limit);
        return Aggregation.newAggregation(operations);
    }

    private long getTotalCount(Criteria criteria) {
        Query query = new Query();
        if (criteria != null) {
            query.addCriteria(criteria);
        }
        //获取总条数
        return this.mongoTemplate.count(query, getTClass());
    }

    @Override
    public boolean save(T entity) {
        try{
            mongoTemplate.save(entity);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean saveList(List<T> list) {
        try{
            mongoTemplate.insertAll(list);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean removeById( Serializable id) {
        try{
            Query id1 = Query.query(Criteria.where("_id").is(id));
            mongoTemplate.remove(id1,getTClass());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean updateById(String[] keys,Object[] values,Serializable id) {
        Criteria criteria=Criteria.where("_id").is(id);
        Query query=new Query(criteria);
        Update update=new Update();
        for (int i = 0; i < keys.length; i++) {
            update.set(keys[i],values[i]);
        }
        try{
            mongoTemplate.updateFirst(query,update,getTClass());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public T findById(Serializable id) {
        T result;
        try {
            Criteria criteria = Criteria.where("_id").is(id);
            Query query = Query.query(criteria);
            result = mongoTemplate.findOne(query, getTClass());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public T findById(Serializable id,String[] fields) {
        T result;
        try {
            Criteria criteria = Criteria.where("_id").is(id);
            Query query = Query.query(criteria);
            org.springframework.data.mongodb.core.query.Field fields1 = query.fields();
            for (String field : fields) {
                fields1.include(field);
            }
            result = mongoTemplate.findOne(query, getTClass());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<T> findAll() {
        List<T> list;
        try{
            list= mongoTemplate.findAll(getTClass());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> findAll(Query query) {
        List<T> list;
        try{
            list= mongoTemplate.find(query,getTClass());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private Class<T> getTClass()
    {
        Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }
}
