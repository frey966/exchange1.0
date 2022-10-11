package com.financia.business.dataconfiguration.mapper;

import com.financia.currency.ExchangeOrder;
import icu.mhb.mybatisplus.plugln.base.mapper.JoinBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 币币交易委托单
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-09-08 14:22:46
 */
@Mapper
public interface ExExchangeOrderMapper extends JoinBaseMapper<ExchangeOrder> {

}
