package com.financia.exchange.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.superleverage.SuperContractOrderEntrust;
import com.financia.swap.ContractOrderEntrustStatus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


/**
 * 合约交易币Mapper接口
 *
 * @author ruoyi
 * @date 2022-07-13
 */
public interface SuperContractOrderEntrustMapper extends BaseMapper<SuperContractOrderEntrust>
{

    /**
     * 更新订单状态
     * @param eid
     * @param status
     */
    @Update("update ex_super_contract_order_entrust contract set contract.status = #{status} where contract.id = #{eid}")
    void updateStatus(@Param("eid") Long eid, @Param("status") ContractOrderEntrustStatus status);

}
