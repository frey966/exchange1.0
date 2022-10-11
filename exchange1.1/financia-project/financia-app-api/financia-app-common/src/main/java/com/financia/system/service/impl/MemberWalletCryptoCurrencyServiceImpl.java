package com.financia.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.currency.MemberCryptoCurrencyWallet;
import com.financia.system.mapper.MemberWalletCryptoCurrencyMapper;
import com.financia.system.service.MemberWalletCryptoCurrencyService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service("memberWalletCryptoCurrencyService")
public class MemberWalletCryptoCurrencyServiceImpl extends ServiceImpl<MemberWalletCryptoCurrencyMapper, MemberCryptoCurrencyWallet> implements MemberWalletCryptoCurrencyService {

//    @Override
//    public boolean createWalletCryptoCurrency(Long memberId) {
//        MemberCryptoCurrencyWallet save = new MemberCryptoCurrencyWallet();
//        save.setMemberId(memberId);
//        save.setCoinId(1);
//        save.setCyptoMoney(new BigDecimal(0l));
//        save.setFreezeMoney(new BigDecimal(0));
//        save.setMoney(new BigDecimal(0));
//        if (save(save)){
//            return true;
//        }
//        return false;
//    }

}
