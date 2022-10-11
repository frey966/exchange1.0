package com.financia.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.PCommonProblem;
import com.financia.system.mapper.PCommonProblemMapper;
import com.financia.system.service.PCommonProblemService;
import org.springframework.stereotype.Service;

@Service("pCommonProblemService")
public class PCommonProblemServiceImpl extends ServiceImpl<PCommonProblemMapper, PCommonProblem> implements PCommonProblemService {
}
