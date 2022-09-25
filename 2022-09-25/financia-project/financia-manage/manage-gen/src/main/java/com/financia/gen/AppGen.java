package com.financia.gen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.financia.common.security.annotation.EnableCustomConfig;
import com.financia.common.security.annotation.EnableRyFeignClients;
import com.financia.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 代码生成
 * 
 * @author ruoyi
 */
@EnableCustomConfig
@EnableCustomSwagger2   
@EnableRyFeignClients
@SpringBootApplication
public class AppGen
{
    public static void main(String[] args)
    {
        //SpringApplication.run(AppGen.class, args);
        SpringApplication application = new SpringApplication(new Class<?> [] {AppGen.class});
        application.setAllowBeanDefinitionOverriding(true);
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  代码生成模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }
}
