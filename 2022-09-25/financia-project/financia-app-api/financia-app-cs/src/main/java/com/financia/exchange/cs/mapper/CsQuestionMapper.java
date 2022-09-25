package com.financia.exchange.cs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.cs.CsQuestion;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客服-自动回复问题
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-09-22 17:05:31
 */
@Mapper
public interface CsQuestionMapper extends BaseMapper<CsQuestion> {

}
