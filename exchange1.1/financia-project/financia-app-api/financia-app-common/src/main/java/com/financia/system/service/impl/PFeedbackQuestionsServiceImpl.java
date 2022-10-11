package com.financia.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.PFeedbackQuestions;
import com.financia.system.mapper.PFeedbackQuestionsMapper;
import com.financia.system.service.PFeedbackQuestionsService;
import org.springframework.stereotype.Service;

@Service("pFeedbackQuestionsService")
public class PFeedbackQuestionsServiceImpl extends ServiceImpl<PFeedbackQuestionsMapper, PFeedbackQuestions> implements PFeedbackQuestionsService {
}
