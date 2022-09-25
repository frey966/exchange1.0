package com.financia.exchange.cs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.cs.CsQuestion;
import com.financia.exchange.cs.mapper.CsQuestionMapper;
import com.financia.exchange.cs.service.CsQuestionService;
import org.springframework.stereotype.Service;


@Service("csQuestionService")
public class CsQuestionServiceImpl extends ServiceImpl<CsQuestionMapper, CsQuestion> implements CsQuestionService {

}
