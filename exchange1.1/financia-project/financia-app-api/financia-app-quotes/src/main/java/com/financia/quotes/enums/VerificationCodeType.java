package com.financia.quotes.enums;

/**

 */
public enum VerificationCodeType
{
    REGISTER(1, "注册发送邮件","EMAIL_BIND_CODE_"), LOGIN(2, "登录发送邮件","EMAIL_LOGIN_CODE_");

    private final int code;
    private final String info;
    private final String prefix;

    VerificationCodeType(int code, String info, String prefix)
    {
        this.code = code;
        this.info = info;
        this.prefix = prefix;
    }

    public int getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }

    public String getPrefix()
    {
        return prefix;
    }
}
