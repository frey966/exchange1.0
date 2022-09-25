package com.financia.common.core.enums;

/**
 * 钱包流水，交易类型
 *
 * @author ruoyi
 */
public enum TradeType
{
    SUBTRACT(1, "支出"),
    ADD(2, "收入"),
    FREEZE(3, "冻结余额"),
    FREEZE_SUBTRACT(4, "扣减冻结余额"),
    FREEZE_BACK(5, "退回冻结额余额")
    ;

    private final int code;
    private final String info;

    TradeType(int code, String info)
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
