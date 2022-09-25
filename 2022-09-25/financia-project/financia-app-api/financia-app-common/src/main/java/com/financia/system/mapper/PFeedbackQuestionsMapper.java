package com.financia.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.common.PFeedbackQuestions;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("pFeedbackQuestionsMapper")
public interface PFeedbackQuestionsMapper extends BaseMapper<PFeedbackQuestions> {
}
