package com.financia.common.core.enums;

/**
 * 发送消息方式
 *
 * @author ruoyi
 */
public enum SendModeType
{
    EMAIL(1, "邮箱"), PHOEN(2, "手机");

    private final int code;
    private final String info;

    SendModeType(int code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public int getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
