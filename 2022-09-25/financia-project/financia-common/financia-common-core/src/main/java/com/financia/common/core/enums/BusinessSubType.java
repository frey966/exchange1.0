package com.financia.common.core.enums;

/**
 * 钱包流水，操作业务类型(小类)
 *
 * @author ruoyi
 */
public enum BusinessSubType
{
    // 合约
    SWAP_OPEN("swap-open", "开仓委托", BusinessType.SWAP),
    SWAP_CLOSE_SUCCESS("swap-close-success", "平仓成功", BusinessType.SWAP),
    SWAP_SUB_SUCCESS("swap-sub-success", "减仓成功", BusinessType.SWAP),
    SWAP_OPEN_SUCCESS("swap-open-success", "开仓成功", BusinessType.SWAP),
    SWAP_ADD_SUCCESS("swap-add-success", "加仓成功", BusinessType.SWAP),
    SWAP_OPEN_CANCEL("swap-open-cancel", "撤销开仓委托", BusinessType.SWAP),

    // 超级杠杆
    SUPER_OPEN("super-open", "开仓委托", BusinessType.SUPER),
    SUPER_OPEN_SUCCESS("super-open-success", "开仓成功", BusinessType.SUPER),
    SUPER_CLOSE_SUCCESS("super-close-success", "平仓成功", BusinessType.SUPER),
    SUPER_OPEN_CANCEL("super-open-cancel", "撤销开仓委托", BusinessType.SUPER),
    SUPER_ADD_SUCCESS("super-add-success", "加仓成功", BusinessType.SUPER),
    SUPER_SUB_SUCCESS("super-sub-success", "减仓成功", BusinessType.SUPER),

    // 币币交易
    BB_BUY("bb-buy", "买入下单", BusinessType.BB),
    BB_SELL_SUCCESS("bb-sell-success", "卖出成功", BusinessType.BB),
    BB_BUY_SUCCESS("bb-buy-success","买入成功", BusinessType.BB),
    BB_BUY_CANCEL("bb-cancel", "撤销买入", BusinessType.BB),

    // 量化理财
    QUANTIZE_FREEZE("quantize-freeze","量化理财冻结",BusinessType.QUANTIZE),
    QUANTIZE_BUY("quantize-buy","量化理财购买扣款",BusinessType.QUANTIZE),
    QUANTIZE_RANSOM("quantize-ransom","量化理财赎回收入",BusinessType.QUANTIZE),

    // 提现
    WITHDRAW_FREEZE("withdraw-freeze","提现冻结余额",BusinessType.WITHDRAW),
    WITHDRAW_DEDUCT("withdraw-deduct","提现冻结扣款",BusinessType.WITHDRAW),

    //充值
    RECHARGE_ADD("recharge_add", "充值增加余额",BusinessType.RECHARGE),
    //汇兑
    EXCHANGE("EXCHANGE_DEDUCT","汇兑扣款",BusinessType.EXCHANGE),
    ;

    private final String code;
    private final String info;
    private final BusinessType parentType;

    BusinessSubType(String code, String info,BusinessType parentType)
    {
        this.code = code;
        this.info = info;
        this.parentType = parentType;
    }

    public String getParentTypeCode() {
        return parentType.getCode();
    }
    public String getParentTypeInfo() {
        return parentType.getInfo();
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
