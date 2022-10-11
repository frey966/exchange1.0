package com.financia.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.exchange.PNotice;
import com.financia.system.mapper.PNoticeMapper;
import com.financia.system.service.PNoticeService;
import org.springframework.stereotype.Service;


@Service("pNoticeService")
public class PNoticeServiceImpl extends ServiceImpl<PNoticeMapper, PNotice> implements PNoticeService {


}
