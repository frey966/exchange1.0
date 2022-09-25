package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.exchange.mapper.CoinFeeMapper;
import com.financia.exchange.mapper.MemberAssetRecordMapper;
import com.financia.exchange.service.CoinFeeService;
import com.financia.exchange.service.MemberAssetRecordService;
import com.financia.swap.CoinFee;
import com.financia.swap.MemberAssetRecord;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

@Service
public class MemberAssetRecordServiceImpl extends ServiceImpl<MemberAssetRecordMapper, MemberAssetRecord> implements MemberAssetRecordService {

}
