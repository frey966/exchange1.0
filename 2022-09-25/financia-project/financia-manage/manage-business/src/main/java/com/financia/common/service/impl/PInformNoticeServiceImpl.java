package com.financia.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.PInformNotice;
import com.financia.common.mapper.PInformNoticeMapper;
import com.financia.common.service.PInformNoticeService;
import org.springframework.stereotype.Service;


@Service("pInformNoticeService")
public class PInformNoticeServiceImpl extends ServiceImpl<PInformNoticeMapper, PInformNotice> implements PInformNoticeService {

}
