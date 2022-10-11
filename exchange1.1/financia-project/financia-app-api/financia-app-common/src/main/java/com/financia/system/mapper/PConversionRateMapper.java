package com.financia.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.exchange.PConversionRate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author putao
 * @email xxxxxx@qq.com
 * @date 2022-07-27 21:13:55
 */
@Mapper
@Repository("pConversionRateMapper")
public interface PConversionRateMapper extends BaseMapper<PConversionRate> {
    @Select("SELECT * FROM p_conversion_rate WHERE currency_symbol=#{currencySymbol}")
    PConversionRate getByCurrencySymbol(@Param("currencySymbol") String currencySymbol);

}
