package com.financia.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.exchange.MemberWithdrawOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 交易所-会员提现申请
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-09 20:53:37
 */
@Mapper
public interface MemberWithdrawOrderMapper extends BaseMapper<MemberWithdrawOrder> {

}
