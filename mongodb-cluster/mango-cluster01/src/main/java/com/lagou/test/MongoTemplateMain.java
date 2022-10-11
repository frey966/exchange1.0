//package com.lagou;
//
//import com.lagou.bean.Grade;
//import com.lagou.bean.Resume;
//import com.lagou.bean.Student;
//import com.lagou.bean.Teacher;
//import com.lagou.dao.ResumeDAO;
//import com.lagou.dao.StudentDao;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ApplicationContext;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//@SpringBootApplication
//public class MongoTemplateMain {
//    public static void main(String[] args) {
//
////        insertResume();
////        findByName();
////        insert();
////        insertGrade();
////        insertTeacher();
//        queryMore();
//    }
//
//    /**
//     * 多表查询
//     */
//    public static void queryMore() {
//        ApplicationContext  applicationContext  = SpringApplication.run(MongoTemplateMain.class);
//        StudentDao  studentDao = applicationContext.getBean("studentDao", StudentDao.class);
//        Object studentAndGrade = studentDao.findStudentAndGrade();
//
//    }
//
//    public static void insert(){
//        ApplicationContext  applicationContext  = SpringApplication.run(MongoTemplateMain.class);
//        StudentDao  studentDao = applicationContext.getBean("studentDao", StudentDao.class);
//        int name = 1;
//        for (int i=1; i < 100; i++) {
//            Student student = new Student();
//            student.setId("" + UUID.randomUUID());
//            student.setName("测试" + name);
//            student.setSex(i > 50? "男": "女");
//            student.setGradeId(i > 50? 2: 1);
//            student.setTeacherId(i > 50? 2: 1);
//            studentDao.saveStudent(student);
//            name ++ ;
//        }
//    }
//    public static void insertTeacher(){
//        ApplicationContext  applicationContext  = SpringApplication.run(MongoTemplateMain.class);
//        StudentDao  studentDao = applicationContext.getBean("studentDao", StudentDao.class);
//        Teacher teacher = new Teacher();
//        teacher.setId(2);
//        teacher.setSex("女");
//        teacher.setTeacherName("沈老师");
//        studentDao.saveTeacher(teacher);
//    }
//    public static void insertGrade(){
//        ApplicationContext  applicationContext  = SpringApplication.run(MongoTemplateMain.class);
//        StudentDao  studentDao = applicationContext.getBean("studentDao", StudentDao.class);
//        Grade grade = new Grade();
//        grade.setId(2);
//        grade.setGradeName("二年级");
//        studentDao.saveGrade(grade);
//    }
//    /*
//    * 添加数据
//    */
//    public static void insertResume(){
//        ApplicationContext  applicationContext  = SpringApplication.run(MongoTemplateMain.class);
//        ResumeDAO  resumeDao = applicationContext.getBean("resumeDao",ResumeDAO.class);
//        Resume  resume  = new Resume();
//        String collection = "chuangzhang02";
//        Date date = null;
//        String  dateStr = "yyyy-MM-dd hh:mm:ss";
//        SimpleDateFormat  simpleDateFormat  = new SimpleDateFormat(dateStr);
//        try {
//            System.out.println("----begin---");
//            for(int i=0;i<2;i++){
//                resume.setId(""+UUID.randomUUID());
//                resume.setBirthday(new Date());
//                resume.setExpectSalary(26000);
//                resume.setName("CCTV");
//                resume.setCity("guangzhou");
//                resumeDao.insertResume(resume,collection);
//            }
//            System.out.println("----end---");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 查询数据
//     *
//     */
//    public static void findByName(){
//        ApplicationContext  applicationContext  = SpringApplication.run(MongoTemplateMain.class);
//        ResumeDAO  resumeDao = applicationContext.getBean("resumeDao",ResumeDAO.class);
//        String collection = "chuangzhang02";
//        Resume  resume1 =resumeDao.findByName("beijing-船长1",collection);
//        System.out.println("resume1=="+resume1);
//        List<Resume> datas1 = resumeDao.findList("beijing-船长0",collection);
//        System.out.println("datas1=="+datas1);
//        List<Resume> datas2 = resumeDao.findListByNameAndExpectSalary("beijing-船长2",28000,collection);
//        System.out.println("datas2=="+datas2);
//    }
//
//
//}
