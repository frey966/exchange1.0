package com.financia.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.business.ExCoin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository("exCoinMapper")
public interface ExCoinMapper extends BaseMapper<ExCoin> {

    @Select("SELECT name FROM ex_coin WHERE can_recharge=#{canRecharge}")
    List<String> getCoinNameByCanRecharge(@Param("canRecharge") Integer canRecharge);

    @Select("SELECT name FROM ex_coin WHERE can_transfer=#{canTransfer}")
    List<String> getCoinNameByCanTransfer(@Param("canTransfer") Integer canTransfer);


}
