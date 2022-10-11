package com.financia.business.contentmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.business.contentmanagement.mapper.PFeedbackQuestionsMapper;
import com.financia.business.contentmanagement.service.PFeedbackQuestionsService;
import com.financia.common.PFeedbackQuestions;
import org.springframework.stereotype.Service;

@Service("pFeedbackQuestionsService")
public class PFeedbackQuestionsServiceImpl extends ServiceImpl<PFeedbackQuestionsMapper, PFeedbackQuestions> implements PFeedbackQuestionsService {
}
