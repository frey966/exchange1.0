package com.financia.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.exchange.PCryptoCurrency;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository("pCryptoCurrencyMapper")
public interface PCryptoCurrencyMapper extends BaseMapper<PCryptoCurrency> {

//    @Select("<script> " +
//            "SELECT ,p.coin_name,p.image_url,p.ranking,p.coin_id FROM" +
//            " p_crypto_currency p" +
//            " WHERE " +
//            " AND p.coin_name IN" +
//            "       <foreach item='coinName' index='index' collection='coinNames' open='(' separator=',' close=')'> " +
//            "          #{coinName} " +
//            "       </foreach>" +
//            "</script>")
//    List<Map> getBycoinNames(@Param("coinNames") List<String> coinNames);

    @Select("SELECT p.coin_name,p.image_url,p.ranking,p.coin_id FROM" +
                    " p_crypto_currency p" +
                    " WHERE " +
                    "  p.coin_name = #{coinName}")
    Map getByCoinName(@Param("coinName") String coinName);
}
