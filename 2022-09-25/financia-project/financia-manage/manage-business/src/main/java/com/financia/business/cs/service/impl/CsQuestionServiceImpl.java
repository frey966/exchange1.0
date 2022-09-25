package com.financia.business.cs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.business.cs.mapper.CsQuestionMapper;
import com.financia.business.cs.service.CsQuestionService;
import com.financia.cs.CsQuestion;
import org.springframework.stereotype.Service;


@Service("csQuestionService")
public class CsQuestionServiceImpl extends ServiceImpl<CsQuestionMapper, CsQuestion> implements CsQuestionService {

}
