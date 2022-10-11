package com.financia.exchange.vo;

import com.financia.currency.MemberCryptoCurrencyWallet;
import com.financia.exchange.MemberBusinessWallet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("币币当前币对持仓")
public class CurrencyWalletVo {

    @ApiModelProperty("其他非0持仓")
    private List<MemberCryptoCurrencyWallet> otherCurrencyWalletList;

    @ApiModelProperty("当前币对持仓(非USDT)")
    private MemberCryptoCurrencyWallet currentCurrencyWallet;


}
