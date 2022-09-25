package com.financia.system.compliance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.PCompliance;
import com.financia.system.compliance.mapper.ComplianceMapper;
import com.financia.system.compliance.service.ComplianceSercice;
import org.springframework.stereotype.Service;

@Service
public class ComplianceServiceImpl extends ServiceImpl<ComplianceMapper, PCompliance> implements ComplianceSercice {
}
