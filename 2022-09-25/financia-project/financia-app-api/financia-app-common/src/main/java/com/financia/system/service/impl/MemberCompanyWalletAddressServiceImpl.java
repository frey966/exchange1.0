package com.financia.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.exchange.MemberCompanyWalletAddress;
import com.financia.system.mapper.MemberCompanyWalletAddressMapper;
import com.financia.system.service.MemberCompanyWalletAddressService;
import org.springframework.stereotype.Service;


@Service("memberCompanyWalletAddressService")
public class MemberCompanyWalletAddressServiceImpl extends ServiceImpl<MemberCompanyWalletAddressMapper, MemberCompanyWalletAddress> implements MemberCompanyWalletAddressService {

}
