package com.financia.common.core.enums;

/**
 * 数据状态
 *
 * @author ruoyi
 */
public enum DataStatus
{
    VALID(1, "有效"), DELETED(0, "删除"), UNENABLE(2, "禁用");

    private final int code;
    private final String info;

    DataStatus(int code, String info)
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
