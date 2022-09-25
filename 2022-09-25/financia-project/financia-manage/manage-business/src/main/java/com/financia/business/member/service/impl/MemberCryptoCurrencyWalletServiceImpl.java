package com.financia.business.member.service.impl;

import com.financia.business.member.mapper.MemberCryptoCurrencyWalletMapper;
import com.financia.business.member.service.MemberCryptoCurrencyWalletService;
import com.financia.currency.MemberCryptoCurrencyWallet;
import icu.mhb.mybatisplus.plugln.base.service.impl.JoinServiceImpl;
import org.springframework.stereotype.Service;


@Service("memberCryptoCurrencyWalletService")
public class MemberCryptoCurrencyWalletServiceImpl extends JoinServiceImpl<MemberCryptoCurrencyWalletMapper, MemberCryptoCurrencyWallet> implements MemberCryptoCurrencyWalletService {

}
