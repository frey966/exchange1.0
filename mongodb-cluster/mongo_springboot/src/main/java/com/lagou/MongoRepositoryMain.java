package com.lagou;

import com.lagou.bean.Resume;
import com.lagou.repository.ResumeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MongoRepositoryMain {
    public static void main(String[] args) {
        ApplicationContext  applicationContext = SpringApplication.run(MongoRepositoryMain.class,args);
        ResumeRepository  resumeRepository = applicationContext.getBean(ResumeRepository.class);
        /* System.out.println(resumeRepository.findAll());
        System.out.println(resumeRepository.findByNameEquals("zhangsan"));
        System.out.println(resumeRepository.findByNameAndExpectSalary("zhangsan",25000)); */
        Resume resume  = new Resume();
        resume.setName("liuxiaosong");
        resume.setExpectSalary(1);
        resume.setCity("shanghai");
        resumeRepository.save(resume);
    }
}
