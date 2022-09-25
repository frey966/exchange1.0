package com.financia.common.core.enums;

/**
 * 转账状态
 * 转账状态：1成功，-1:失败 0:未开始 2:转账中
 * @author ruoyi
 */
public enum TransStatus
{
    succeed(1, "1成功"), fail(-1, "失败"), unplayed(0, "未开始"), underway(2,"转账中");

    private final int code;
    private final String info;

    TransStatus(int code, String info)
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
