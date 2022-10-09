package com.lagou.dao;

import com.lagou.bean.Resume;

import java.util.List;

public interface ResumeDAO {
    public void  insertResume(Resume resume,String collection);
    /** 根据name 获取Resume 对象 */
    Resume  findByName(String name,String collection);
    List<Resume> findList(String name,String collection);
    /** 根据name  和  expectSalary 查询 */

    public List<Resume> findLikeList(String name,String collection);

    List<Resume> findListByNameAndExpectSalary(String name, double expectSalary,String collection);

    public List<Resume> getGoodsDetails1(int begin, int end,String collection);

    public List<Resume> getGoodsDetails2(String collection);

    public List<Resume> getGoodsDetails3(int begin, int end,String collection);

    public long getGoodsDetails4(int begin, int end,String collection);

    public void getGoodsDetails5(int begin, int end,String collection);

    public void getGoodsDetails6(Resume resume,String collection);

    public void updateProcessLandLog(Resume resume, List<String> fields,List<Object> values,String collection);

    public void deleteObject(Class<Object> clazz,String id,String collection);


    public List<Resume> findByPage(int page,int limit,String name,String collection);

}
