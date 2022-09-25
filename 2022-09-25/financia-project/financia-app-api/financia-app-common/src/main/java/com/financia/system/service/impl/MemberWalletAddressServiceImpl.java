package com.financia.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.core.enums.DataStatus;
import com.financia.exchange.Member;
import com.financia.exchange.MemberWalletAddress;
import com.financia.system.crypto.Manager;
import com.financia.system.crypto.bean.CryptoBean;
import com.financia.system.mapper.MemberWalletAddressMapper;
import com.financia.system.service.MemberWalletAddressService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service("memberWalletAddressService")
public class MemberWalletAddressServiceImpl extends ServiceImpl<MemberWalletAddressMapper, MemberWalletAddress> implements MemberWalletAddressService {

    @Override
    public boolean createWalletAddress(Member save) {
        //c1:btc链（比特币 2:eth 链（以太坊）3:trx链（波场）
        if (saveWalletAddress(save, Manager.Type.TRC20,  Manager.Type.TRC20.getCode(), "TRC20")) {return false;}
        if (saveWalletAddress(save, Manager.Type.HECO, Manager.Type.HECO.getCode(), "HECO")) {return false;}
        if (saveWalletAddress(save, Manager.Type.BSC,  Manager.Type.BSC.getCode(), "BSC")) {return false;}
        if (saveWalletAddress(save, Manager.Type.ERC20,  Manager.Type.ERC20.getCode(), "ERC20")) {return false;}
        return true;
    }

    /**
     * 保存链
     * @param save
     * @param type
     * @param i
     * @param trx2
     * @return
     */
    private boolean saveWalletAddress(Member save, Manager.Type type, int i, String trx2) {
        CryptoBean bean= Manager.getInstance().get(type);
        MemberWalletAddress address = new MemberWalletAddress();
        address.setAddress(bean.getAddress());
        address.setPrivateKey(bean.getPrivateKey());
        address.setMemberId(save.getId());
        address.setStatus(DataStatus.VALID.getCode());
        address.setSumRechargeMoney(new BigDecimal(0l));
        address.setTradeCount(0);
        address.setChainId(i);
        address.setChainName(trx2);
        boolean save1 = this.save(address);
        if (!save1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean addRechargeMoney(Integer id, BigDecimal money) {
        MemberWalletAddress walletAddress = getById(id);
        MemberWalletAddress update = new MemberWalletAddress();
        update.setId(id);
        if (walletAddress.getTradeCount() == null) {
            walletAddress.setTradeCount(0);
        }
        update.setTradeCount(walletAddress.getTradeCount() + 1);
        if (walletAddress.getSumRechargeMoney() == null) {
            walletAddress.setSumRechargeMoney(new BigDecimal(0l));
        }
        update.setSumRechargeMoney(walletAddress.getSumRechargeMoney().add(money));
        if (updateById(update)) {
            return true;
        }
        return false;
    }
}
