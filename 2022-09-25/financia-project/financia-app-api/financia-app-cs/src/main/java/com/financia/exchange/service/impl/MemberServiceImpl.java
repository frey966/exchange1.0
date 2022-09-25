package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.exchange.Member;
import com.financia.exchange.mapper.MemberMapper;
import com.financia.exchange.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员信息Service业务层处理
 *
 * @author ruoyi
 * @date 2022-07-13
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService
{

}
