package com.financia.exchange;

import com.financia.core.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Getter
public enum AssetRecordTypeEnum implements BaseEnum {
    CONTRACT_TRADE(1,"合约交易"),
    SUPER_LEVER_TRADE(2,"超级杠杆交易"),
    QUANTIFY_TRADE(3,"量化交易"),
    STOCK_TRADE(4,"股票交易"),
    MARKET_TRADE(5,"币币交易"),

    ;


    private Integer code;
    @Setter
    private String name;

    @Override
    public int getOrdinal() {
        return this.ordinal();
    }
}
