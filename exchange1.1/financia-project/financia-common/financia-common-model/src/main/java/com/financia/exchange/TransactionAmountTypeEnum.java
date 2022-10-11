package com.financia.exchange;

import com.fasterxml.jackson.annotation.JsonValue;
import com.financia.core.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum TransactionAmountTypeEnum implements BaseEnum {
    CONTRACT_PROFIT_LOSS("合约盈亏"),
    CONTRACT_OPEN_BET_BUY("开仓买入"),
    CONTRACT_OPEN_BET_SELL("开仓卖出"),
    CONTRACT_CLOSE_BET_BUY("平仓买入"),
    CONTRACT_CLOSE_BET_SELL("平仓卖出"),
    MANUAL_DEPOSIT("币币自动充值"),
    MANUAL_DEPOSIT_CONFIRM("币币确认充值"),
    BACKSTAGE_DEPOSIT("后台人工充币"),
    OPTIONS_RISE("投注【涨】"),
    OPTIONS_FALL("投注【跌】"),
    OPTIONS_PING("投注【平】"),
    PLATFORM_TAKES_ALL("平台通吃"),
    THREE_PARTY_RECHARGE("三方充值"),
    BIBI_BY("币币买入"),
    BIBI_SELL("币币卖出"),
    WITHDRAW_ARTIFICIAL("提币"),
    BIBI_TO_CONTRACT("币币账户-->合约账户"),
    BIBI_TO_GAME("币币账户-->游戏账户"),
    CONTRACT_TO_GAME("合约账户-->游戏账户"),
    CONTRACT_TO_BIBI("合约账户-->币币账户"),
    GAME_TO_BIBI("游戏账户-->币币账户"),
    GAME_TO_CONTRACT("游戏账户-->合约账户"),
    CONTRACT_TO_CONTRACT("合约之间划转"),
    BIBI_PROMOTION_COMMISSION("推广佣金"),
    FIAT_CURRENCY_BUY("法币买入"),
    FIAT_CURRENCY_SELL("法币卖出"),
    CQU_MINUTE("猜区块"),

    SUB_CONTRACT("减仓"),
    ADD_CONTRACT("加仓"),

    FUNDING_FEE("资金费"),


    STOP_LOSS("止损"),
    STOP_PROFIT("止盈");





    @Setter
    private String name;

    @Override
    @JsonValue
    public int getOrdinal(){
        return this.ordinal();
    }
}
