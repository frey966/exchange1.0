package com.financia.common.core.enums;

/**
 * 钱包类型
 *
 * @author ruoyi
 */
public enum WalletType
{
    BALANCE(1, "余额");

    private final int code;
    private final String info;

    WalletType(int code, String info)
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
