package com.financia.exchange.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.swap.ContractOrderEntrust;
import com.financia.swap.ContractOrderEntrustStatus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


/**
 * 合约交易币Mapper接口
 *
 * @author ruoyi
 * @date 2022-07-13
 */
public interface ContractOrderEntrustMapper extends BaseMapper<ContractOrderEntrust>
{

    /**
     * 更新订单状态
     * @param eid
     * @param status
     */
    @Update("update ex_contract_order_entrust contract set contract.status = #{status}, contract.triggering_time=#{updateTime} where contract.id = #{eid}")
    void updateStatus(@Param("eid") Long eid, @Param("status") ContractOrderEntrustStatus status, @Param("updateTime") Long time);

}
