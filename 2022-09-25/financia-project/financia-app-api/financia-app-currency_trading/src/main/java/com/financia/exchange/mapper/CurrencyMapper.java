package com.financia.exchange.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.currency.Currency;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface CurrencyMapper extends BaseMapper<Currency> {

    /**
     * 获取可用交易对列表
     * @return
     */
    @Select("SELECT * FROM ex_currency where status=1 order by sort asc")
    List<Currency> querySymbolList();

    @Select("SELECT * FROM ex_currency WHERE status=1 AND symbol=#{symbol}")
    Currency queryCurrencyInfoBySymbol(@Param("symbol") String symbol);
}