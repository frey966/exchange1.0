package com.financia.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.exchange.AgenRelation;
import com.financia.system.mapper.AgenRelationMapper;
import com.financia.system.service.AgenRelationService;
import org.springframework.stereotype.Service;


@Service("agenRelationService")
public class AgenRelationServiceImpl extends ServiceImpl<AgenRelationMapper, AgenRelation> implements AgenRelationService {


}
