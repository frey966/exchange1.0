package com.financia.business.member.service.impl;

import com.financia.business.MemberContractWalletEntity;
import com.financia.business.member.mapper.MemberContractWalletMapper;
import com.financia.business.member.service.MemberContractWalletService;
import icu.mhb.mybatisplus.plugln.base.service.impl.JoinServiceImpl;
import org.springframework.stereotype.Service;


@Service("memberContractWalletService")
public class MemberContractWalletServiceImpl extends JoinServiceImpl<MemberContractWalletMapper, MemberContractWalletEntity> implements MemberContractWalletService {

}
