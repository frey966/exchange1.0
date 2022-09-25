package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.exchange.MemberTransaction;
import com.financia.exchange.mapper.MemberTransactionMapper;
import com.financia.exchange.service.MemberTransactionService;
import org.springframework.stereotype.Service;

@Service
public class MemberTransactionServiceImpl extends ServiceImpl<MemberTransactionMapper, MemberTransaction> implements MemberTransactionService {

}
