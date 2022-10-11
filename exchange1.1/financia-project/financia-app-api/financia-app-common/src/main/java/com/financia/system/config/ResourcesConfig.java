package com.financia.system.config;

import com.financia.common.core.utils.i18n.LocaleMessageSourceUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 通用映射配置
 *
 * @author ruoyi
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
    /**
     * 开启跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路由
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 设置允许的方法
                //.allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"})
                .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"});
    }

    /**
     * 国际化
     *
     * @return
     */
    @Bean(name = "messageSource")
    public ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setDefaultEncoding("UTF-8");
        resourceBundleMessageSource.setBasenames("i18n/messages", "i18n/ValidationMessages");
        resourceBundleMessageSource.setCacheSeconds(3600);
        return resourceBundleMessageSource;
    }

    /**
     * 国际化工具
     * @return
     */
    @Bean(name = "localeMessageSourceUtil")
    public LocaleMessageSourceUtil getLocaleMessageSourceUtil() {
        LocaleMessageSourceUtil localeMessageSourceUtil = new LocaleMessageSourceUtil();
        return localeMessageSourceUtil;
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(getMessageSource());
        return validator;
    }

}
