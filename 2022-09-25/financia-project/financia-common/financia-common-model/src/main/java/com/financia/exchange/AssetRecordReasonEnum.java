package com.financia.exchange;


import com.financia.core.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AssetRecordReasonEnum implements BaseEnum {

    CONTRACT_OPEN("合约开仓"), //0
    CONTRACT_CLOSE("合约平仓"), //1
    CONTRACT_ADD("合约加仓"), //2
    CONTRACT_SUB("合约减仓"), //3
    CONTRACT_SUB_PRINCIPLE("合约减仓减少保证金"),//4
    CONTRACT_SUB_COIN("合约减仓减少币"), //5
    CONTRACT_LOSS("合约亏损"), //6
    CONTRACT_PROFIT("合约盈利"), //7
    EXCHANGE_LOSS("交易所损失"), //8
    CONTRACT_SPOT("合约爆仓"), //9
    CONTRACT_ASSET_TRANSFER("资金费率转移"), //10
    CONTRACT_STOP_LOSS("止损"), //11
    CONTRACT_STOP_PROFIT("止盈"), //12
    CONTRACT_FORCE_CLOSE_EXCHANGE_LOSS("强制平仓穿仓损失"), //13

    OPEN_FEE("开仓手续费"), //14

    CLOSE_FEE("平仓手续费"), //15
    MARKET_MACH_BUY("币币买单"), //16
    MARKET_MACH_SELL("币币买单") //17
    ;

    private String reason;
    @Override
    public int getOrdinal() {
        return this.getOrdinal();
    }
}
