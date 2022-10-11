package com.financia.exchange.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.swap.ContractOrderDirection;
import com.financia.swap.ContractOrderEntrust;
import com.financia.swap.ContractOrderEntrustStatus;

import java.util.List;

public interface ContractOrderEntrustService extends IService<ContractOrderEntrust> {

    /**
     * 查询未撮合的订单
     * @param contractId
     * @return
     */
    List<ContractOrderEntrust> loadUnMatchOrders(Long contractId);

    /**
     * 根据合约ID查询所有平中的订单
     * @param memberId
     * @return
     */
    List<ContractOrderEntrust> queryAllEntrustClosingOrdersByContractCoin(Long memberId, Long contractId, ContractOrderDirection direction);

    /**
     * 更新订单状态
     * @param id
     * @param status
     */
    void updateStatus(Long id, ContractOrderEntrustStatus status, Long time);

    /**
     * 查询指定用户指定币种当前所有开仓委托(分页)
     * @param memberId
     * @param contractCoinId
     * @return
     */
    Page<ContractOrderEntrust> queryPageEntrustingOrdersBySymbol(Long memberId, Long contractCoinId, Integer type, int pageNo, int pageSize);

    /**
     * 查询某个会员的某个交易对的委托中订单
     * @param member
     * @param contractCoinId
     * @return
     */
    List<ContractOrderEntrust> findEntrustingOrderByMemberIdAndContractId(Long memberId, Long contractCoinId);

    /**
     * 保存开仓订单并且冻结余额
     * @param order 开仓委托单
     * @return
     */
    int saveOpenOrderAndFreezeBalance(ContractOrderEntrust order);


}
