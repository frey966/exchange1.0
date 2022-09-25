package com.financia.common.core.enums;

/**

 */
public enum VerificationCodeType
{
    REGISTER("register", "注册发送邮件","REGISTER_CODE_", "bindCodeEmail.ftl", "您的注册验证码是code，请不要把验证码泄漏给其他人，如非本人请勿操作。"),
    LOGIN("login", "登录发送邮件","LOGIN_CODE_","loginCodeEmail.ftl", "您的登陆验证码是code，请不要把验证码泄漏给其他人，如非本人请勿操作。"),
    BACK_PASSWORD("back_password", "找回密码发送邮件","BACK_PASSWORD_","loginCodeEmail.ftl", "您的验证码是code，请不要把验证码泄漏给其他人，如非本人请勿操作。"),
    BIND_PHONE("bind_phone", "绑定手机发送短信","BIND_PHONE_","loginCodeEmail.ftl", "你正在绑定手机,您的验证码是code，请不要把验证码泄漏给其他人，如非本人请勿操作。"),
    BIND_EMAIL("bind_email", "绑定邮箱发送邮件","BIND_EMAIL","loginCodeEmail.ftl", "你正在绑定邮箱,您的验证码是code，请不要把验证码泄漏给其他人，如非本人请勿操作。"),
    UPDATE_PHONE("update_phone", "换绑手机发送短信","BIND_PHONE_","loginCodeEmail.ftl", "你正在更改绑定手机,您的验证码是code，请不要把验证码泄漏给其他人，如非本人请勿操作。"),
    UPDATE_EMAIL("update_email", "换绑邮箱发送邮件","BIND_EMAIL","loginCodeEmail.ftl", "你正在更改绑定邮箱,您的验证码是code，请不要把验证码泄漏给其他人，如非本人请勿操作。"),
    SET_PAYPASSWORD("set_paypassword", "设置支付密码发送邮件","SET_PAYPASSWORD","loginCodeEmail.ftl", "您的验证码是code，请不要把验证码泄漏给其他人，如非本人请勿操作。");


    private final String action;
    private final String info;
    private final String prefix;
    private final String template;
    private final String smsTemplate;


    VerificationCodeType(String action, String info, String prefix,String template,String smsTemplate)
    {
        this.action = action;
        this.info = info;
        this.prefix = prefix;
        this.template = template;
        this.smsTemplate = smsTemplate;
    }

    public String getAction()
    {
        return action;
    }

    public String getInfo()
    {
        return info;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getTemplate() {
        return template;
    }

    public String getSmsTemplate() {
        return smsTemplate;
    }
}
