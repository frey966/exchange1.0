package com.financia.common.core.enums;

/**
 * 钱包流水，操作业务类型(大类)
 *
 * @author ruoyi
 */
public enum BusinessType
{
    SWAP("swap", "合约类型"),
    SUPER("super", "超级杠杆"),
    BB("bb", "币币交易"),
    QUANTIZE("quantize","量化理财"),
    WITHDRAW("withdraw","提现"),
    RECHARGE("recharge","充值"),
    EXCHANGE("exchange","汇兑")
    ;

    private final String code;
    private final String info;

    BusinessType(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
