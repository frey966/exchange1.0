//package com.lagou.test;
//
//import com.lagou.MongoTemplateMain;
//import com.lagou.bean.Resume;
//import com.lagou.dao.ResumeDAO;
//import org.springframework.boot.SpringApplication;
//import org.springframework.context.ApplicationContext;
//
//import java.util.List;
//
//public class MongoReadTemplateMain {
//
//    public static void main(String[] args) {
//        /* ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml"); */
//        ApplicationContext applicationContext = SpringApplication.run(MongoTemplateMain.class, args);
//        ResumeDAO resumeDao = applicationContext.getBean("resumeDao", ResumeDAO.class);
//       // List<Resume> datas = resumeDao.findLikeList("lisi-船长");
//       // System.out.println(datas.size());
//        /*List<Resume> datas2 = resumeDao.findListByNameAndExpectSalary("zhangsan",25000);
//        System.out.println(datas2);*/
//    }
//}
