package com.lagou.test;

import com.lagou.bean.Resume;
import com.lagou.dao.ResumeDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class MongoTemplateMain {
    public static void main(String[] args) {

        insertResume();
        //findByName();

    }
    /*
    * 添加数据
    */
    public static void insertResume(){
        ApplicationContext  applicationContext  = SpringApplication.run(MongoTemplateMain.class);
        ResumeDAO  resumeDao = applicationContext.getBean("resumeDao",ResumeDAO.class);
        Resume  resume  = new Resume();
        String collection = "chuangzhang02";
        Date date = null;
        String  dateStr = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat  simpleDateFormat  = new SimpleDateFormat(dateStr);
        try {
            System.out.println("----begin---");
            for(int i=0;i<2;i++){
                resume.setId(""+UUID.randomUUID());
                resume.setBirthday(new Date());
                resume.setExpectSalary(26000);
                resume.setName("CCTV");
                resume.setCity("guangzhou");
                resumeDao.insertResume(resume,collection);
            }
            System.out.println("----end---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询数据
     *
     */
    public static void findByName(){
        ApplicationContext  applicationContext  = SpringApplication.run(MongoTemplateMain.class);
        ResumeDAO  resumeDao = applicationContext.getBean("resumeDao",ResumeDAO.class);
        String collection = "chuangzhang02";
        Resume  resume1 =resumeDao.findByName("beijing-船长1",collection);
        System.out.println("resume1=="+resume1);
        List<Resume> datas1 = resumeDao.findList("beijing-船长0",collection);
        System.out.println("datas1=="+datas1);
        List<Resume> datas2 = resumeDao.findListByNameAndExpectSalary("beijing-船长2",28000,collection);
        System.out.println("datas2=="+datas2);
    }


}
