package com.financia.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.system.SysDictData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 字典表 数据层
 * 
 * @author ruoyi
 */
@Mapper
@Repository("sysDictDataMapper")
public interface SysDictDataMapper extends BaseMapper<SysDictData> {
}
