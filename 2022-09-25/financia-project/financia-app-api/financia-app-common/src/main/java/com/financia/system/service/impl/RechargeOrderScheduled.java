package com.financia.system.service.impl;

import com.financia.system.service.MemberRechargeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RechargeOrderScheduled {

    @Autowired
    private MemberRechargeOrderService rechargeOrderService;

//    @Scheduled (fixedDelay = 5000)
//    public void updateRechargeBSC() {
//        rechargeOrderService.updateAllUp(Manager.Type.BSC.getCode());
//    }
//    @Scheduled (fixedDelay = 5000)
//    public void updateRechargeERC20() {
//        rechargeOrderService.updateAllUp(Manager.Type.ERC20.getCode());
//    }
//    @Scheduled (fixedDelay = 5000)
//    public void updateRechargeTRC20() {
//        rechargeOrderService.updateAllUp(Manager.Type.TRC20.getCode());
//    }
//    @Scheduled (fixedDelay = 5000)
//    public void updateRechargeHECO() {
//        rechargeOrderService.updateAllUp(Manager.Type.HECO.getCode());
//    }
//    @Scheduled (fixedDelay = 5000)
//    public void updateTransferStatus() {
//        rechargeOrderService.updateAllTransferStatus();
//    }
}
