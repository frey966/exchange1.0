package com.lagou.dao.impl;

import com.lagou.bean.Resume;
import com.lagou.dao.ResumeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("resumeDao")
public class ResumeDAOImpl implements ResumeDAO {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void insertResume(Resume resume,String collection) {
        //mongoTemplate.insert(resume);
        mongoTemplate.insert(resume,collection);
    }

    //插入多条数据
    public void saveObjects(List<Resume> objects,String collection) {
        for(Resume resume:objects){
            mongoTemplate.insert(resume,collection);
        }
    }

   //模糊查询:-----关键字---regex
   /* public long getProcessLandLogsCount(List<Condition> conditions)
    {
        Query query = new Query();
        if (conditions != null && conditions.size() > 0) {
            for (Condition condition : conditions) {
                query.addCriteria(Criteria.where(condition.getKey()).regex(".*?\\" +condition.getValue().toString()+ ".*"));
            }
        }
        return count(query, ProcessLandLog.class);
    }*/

    //查询字段不存在的数据 -----关键字---not
    public List<Resume> getGoodsDetails1(int begin, int end,String collection) {
        Query query = new Query();
        query.addCriteria(Criteria.where("goodsSummary").not());
        return mongoTemplate.find(query.limit(end - begin).skip(begin),Resume.class,collection);
    }
    //查询字段不为空的数据     -----关键字---ne
    public List<Resume> getGoodsDetails2(String collection) {
        Query query = new Query();
        query.addCriteria( Criteria.where("key1").ne("").ne(null));
        List<Resume> datas = mongoTemplate.find(query,Resume.class,collection);
        return datas;
    }

    //查询或语句：a || b     ----- 关键字---orOperator
    public List<Resume> getGoodsDetails3(int begin, int end,String collection) {
        Query query = new Query();
        Criteria.where("key1").ne("").ne(null);
        return mongoTemplate.find(query.limit(end - begin).skip(begin),Resume.class,collection);
    }

    //查询且语句：a && b     ----- 关键字---and
    public long getGoodsDetails4(int begin, int end,String collection) {
        Criteria criteria = new Criteria();
        criteria.and("key1").is(false);
        criteria.and("key2").is("key2");
        Query query = new Query(criteria);
        long totalCount = this.mongoTemplate.count(query, Resume.class,collection);
        return totalCount;
    }

      //5. 查询数量:----- 关键字---count
      /*public long getPageInfosCount(List<Condition> conditions) {
          Query query = new Query();
          if (conditions != null && conditions.size() > 0) {
              for (Condition condition : conditions) {
                  query.addCriteria(Criteria.where(condition.getKey()).is(condition.getValue()));
              }
          }
          return count(query, PageInfo.class);
      }*/

    //查找包含在某个集合范围：----- 关键字---in

    public void getGoodsDetails5(int begin, int end,String collection) {
        Criteria criteria = new Criteria();
        Object [] o = new Object[]{0, 1, 2}; //包含所有
        criteria.and("type").in(o);
        Query query = new Query(criteria);
        //query.with(new Sort(new Sort.Order(Direction.ASC, "type"))).with(new Sort(new Sort.Order(Direction.ASC, "title")));
        List<Resume> list = this.mongoTemplate.find(query, Resume.class,collection);
    }

    //6. 更新一条数据的一个字段
    public void getGoodsDetails6(Resume resume,String collection) {
        String id = resume.getId();
        mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), Update.update("city", resume.getCity()), Resume.class,collection);
    }

    //7. 更新一条数据的多个字段:
    //更新
    public void updateProcessLandLog(Resume resume, List<String> fields,List<Object> values,String collection) {
        Update update = new Update();
        int size = fields.size();
        for(int i = 0 ; i < size; i++){
            String field = fields.get(i);
            Object value = values.get(i);
            update.set(field, value);
        }
        mongoTemplate.updateFirst(new Query(Criteria.where("id").is(resume.getId())), update,Resume.class,collection);
    }

    //8. 删除数据:
    public void deleteObject(Class<Object> clazz,String id,String collection) {
        mongoTemplate.remove(new Query(Criteria.where("id").is(id)),Object.class,collection);
    }



    @Override
    public Resume findByName(String name,String collection) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        List<Resume> datas = mongoTemplate.find(query,Resume.class,collection);
        return  datas.isEmpty()?null:datas.get(0);
    }

    @Override
    public List<Resume> findList(String name,String collection) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        List<Resume> datas = mongoTemplate.find(query,Resume.class,collection);
        return  datas;
    }

    public List<Resume> findLikeList(String name,String collection) {
        Query query = new Query();
        //query.addCriteria(Criteria.where("name").is(name));
        Criteria.where("name").regex(".*?" + name + ".*");
        List<Resume> datas = mongoTemplate.find(query,Resume.class,collection);
        return  datas;
    }



    @Override
    public List<Resume> findListByNameAndExpectSalary(String name, double expectSalary,String collection) {
        Query query = new Query();
        //query.addCriteria(Criteria.where("name").is(name).andOperator(Criteria.where("expectSalary").is(expectSalary)));
        query.addCriteria(Criteria.where("name").is(name).andOperator(Criteria.where("expectSalary").is(expectSalary)));
        return  mongoTemplate.find(query,Resume.class,collection);
    }

    /**
     * 分页模块
     * @param page
     * @param limit
     * @param name
     * @return
     */
    public List<Resume> findByPage(int page,int limit,String name,String collection) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC,"id"));
        Criteria.where("name").regex(".*?" + name + ".*");
        query.skip(page).limit(limit);
        List<Resume> students = mongoTemplate.find(query, Resume.class,collection);
        return students;
    }
}
