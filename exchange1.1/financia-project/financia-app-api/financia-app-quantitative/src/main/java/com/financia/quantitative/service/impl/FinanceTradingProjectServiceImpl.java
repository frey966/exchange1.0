package com.financia.quantitative.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.finance.FinanceTradingProject;
import com.financia.quantitative.mapper.FinanceTradingProjectMapper;
import com.financia.quantitative.service.FinanceTradingProjectService;
import org.springframework.stereotype.Service;


@Service("financeTradingProjectService")
public class FinanceTradingProjectServiceImpl extends ServiceImpl<FinanceTradingProjectMapper, FinanceTradingProject> implements FinanceTradingProjectService {


}
