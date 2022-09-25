package com.financia.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.currency.MemberCryptoCurrencyWallet;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 会员-会员加密货币钱包
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-11 21:48:30
 */
@Mapper
@Repository("memberWalletCryptoCurrencyMapper")
public interface MemberWalletCryptoCurrencyMapper extends BaseMapper<MemberCryptoCurrencyWallet> {


}
