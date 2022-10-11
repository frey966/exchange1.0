package com.financia.system.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.dtflys.forest.http.ForestResponse;
import com.financia.common.PVerificationCode;
import com.financia.common.core.enums.SendModeType;
import com.financia.common.core.enums.VerificationCodeType;
import com.financia.common.core.exception.ServiceException;
import com.financia.common.core.utils.uuid.GeneratorUtil;
import com.financia.common.redis.service.RedisService;
import com.financia.system.service.PVerificationCodeService;
import com.financia.system.service.VerificationCodeService;
import com.financia.system.util.HttpForest;
import com.financia.system.util.SmsParam;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private RedisService redisService;
    @Autowired
    private PVerificationCodeService pVerificationCodeService;
    @Value("${spring.mail.username}")
    private String from;
    @Value("${sms.appkey:CadNqNX5}")
    private String appkey;
    @Value("${sms.secretkey:54PE1Vyf}")
    private String secretkey;
    @Autowired
    private HttpForest httpForest;

    /**
     * 邮箱发送生成验证码
     * @param email
     */
    @Override
    public boolean emailSendCode(String email, VerificationCodeType type) {
        String code = String.valueOf(GeneratorUtil.getRandomNumber(100000, 999999));
        try {
            if (!saveVerificationCode(email, type, SendModeType.EMAIL, code)){
                return false;
            }
            sentEmailCode(email,code,type);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("",e);
            throw new ServiceException("Description Failed to send the verification code");
        }
    }

    /**
     * 手机发送生成验证码
     * @param phone
     */
    @Override
    public boolean phoneSendCode(String phone, VerificationCodeType type) {
        String code = String.valueOf(GeneratorUtil.getRandomNumber(100000, 999999));
        try {
            if (!saveVerificationCode(phone, type, SendModeType.PHOEN, code)){
                return false;
            }
            String smsTemplate = type.getSmsTemplate();
            smsTemplate = smsTemplate.replace("code", code);
            //TODO: 发送手机验证码
            SmsParam smsParam = new SmsParam();
            smsParam.setAppkey(appkey);
            smsParam.setSecretkey(secretkey);
            smsParam.setPhone(phone);
            smsParam.setContent(smsTemplate);
            ForestResponse<Map> forestResponse = httpForest.sendSms(smsParam);
            if (forestResponse.isSuccess()) {
                System.out.println(JSONUtils.toJSONString(forestResponse.getResult()));
                Map result = forestResponse.getResult();
                if (result.get("code").toString().equals("0")){
                    log.error(result.get("result").toString());
                    return true;
                }
                return false;
            } else {
                System.out.println(JSONUtils.toJSONString(forestResponse.getResult()));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("",e);
            throw new ServiceException("Description Failed to send the verification code");
        }
    }

    /**
     * 保存验证码 redis ，数据库
     * @param contact
     * @param verificationCodeType
     */
    private boolean saveVerificationCode(String contact, VerificationCodeType verificationCodeType, SendModeType sendModeType, String code) {
        PVerificationCode pVerificationCode = new PVerificationCode();
        pVerificationCode.setCreateTime(System.currentTimeMillis());
        pVerificationCode.setContact(contact);
        pVerificationCode.setCode(code);
        pVerificationCode.setAction(verificationCodeType.getAction());
        pVerificationCode.setType(sendModeType.getCode());
        boolean save = pVerificationCodeService.save(pVerificationCode);
        if (!save){
            return false;
        }
        redisService.setCacheObject(verificationCodeType.getPrefix() + contact,code,10l, TimeUnit.MINUTES);
        Object cacheObject = redisService.getCacheObject(verificationCodeType.getPrefix() + contact);
        if (cacheObject == null){
            return false;
        }
        return true;
    }

    @Async
    public void sentEmailCode(String email, String code, VerificationCodeType verificationCodeType) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper ;
        helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setTo(email);
        helper.setSubject("");
        Map<String, Object> model = new HashMap<>(16);
        model.put("code", code);
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
        cfg.setClassForTemplateLoading(this.getClass(), "/templates");
        Template template;
        template = cfg.getTemplate(verificationCodeType.getTemplate());
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        helper.setText(html, true);
        //发送邮件
        javaMailSender.send(mimeMessage);
        log.info("send email for {},content:{}", email, html);
    }

    @Override
    public boolean verify (String verificationCode, VerificationCodeType codeType, String suffix) {
        Object verifCode = redisService.getCacheObject(codeType.getPrefix() + suffix);
        if (verifCode == null || !verifCode.toString().equals(verificationCode)){
            return false;
        }
        redisService.deleteObject(codeType.getPrefix() + suffix);
        return true;
    }
}
