package com.financia.common.core.enums;

/**
 * 充值状态
 * 充值状态，0未成功,1:成功
 * @author ruoyi
 */
public enum RechargeStatus
{
    succeed(1, "1成功"), unplayed(0, "失败");

    private final int code;
    private final String info;

    RechargeStatus(int code, String info)
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
