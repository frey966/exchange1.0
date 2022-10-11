package com.financia.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.common.PCommonProblem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("pCommonProblemMapper")
public interface PCommonProblemMapper extends BaseMapper<PCommonProblem> {
}
