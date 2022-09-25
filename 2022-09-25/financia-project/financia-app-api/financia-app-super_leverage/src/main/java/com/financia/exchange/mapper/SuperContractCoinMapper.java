package com.financia.exchange.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.superleverage.SuperContractCoin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * 超级杠杆交易币Mapper接口
 *
 * @author ruoyi
 * @date 2022-07-13
 */
public interface SuperContractCoinMapper extends BaseMapper<SuperContractCoin>
{

    /**
     * 增加平台开仓手续费
     * @param cid
     * @param openFee
     */
    @Update("update ex_super_contract_coin coin set coin.total_open_fee=coin.total_open_fee+#{openFee} where coin.id = #{cid}")
    void increaseOpenFee(@Param("cid")Long cid, @Param("openFee") BigDecimal openFee);

    /**
     * 增加平台平仓手续费
     * @param cid
     * @param closeFee
     */
    @Update("update ex_super_contract_coin coin set coin.total_close_fee=coin.total_close_fee+#{closeFee} where coin.id = #{cid}")
    void increaseCloseFee(@Param("cid")Long cid, @Param("closeFee")BigDecimal closeFee);

    /**
     * 增加平台亏损
     * @param cid
     * @param amount
     */
    @Update("update ex_super_contract_coin coin set coin.total_loss=coin.total_loss+#{amount} where coin.id = #{cid}")
    void increaseTotalLoss(@Param("cid")Long cid, @Param("amount")BigDecimal amount);

    /**
     * 增加平台盈利
     * @param cid
     * @param amount
     */
    @Update("update ex_super_contract_coin coin set coin.total_profit=coin.total_profit+#{amount} where coin.id = #{cid}")
    void increaseTotalProfit(@Param("cid")Long cid, @Param("amount")BigDecimal amount);


}
