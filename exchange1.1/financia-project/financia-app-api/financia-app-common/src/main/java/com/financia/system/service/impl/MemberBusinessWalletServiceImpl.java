package com.financia.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.enums.DataStatus;
import com.financia.common.core.enums.TradeType;
import com.financia.common.core.enums.WalletType;
import com.financia.common.core.exception.ServiceException;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.Member;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.PConversionRate;
import com.financia.system.api.RemoteQuantitativeService;
import com.financia.system.api.RemoteSuperLeverageService;
import com.financia.system.api.RemoteSwapService;
import com.financia.system.mapper.*;
import com.financia.system.service.IMemberService;
import com.financia.system.service.MemberBusinessWalletRecordService;
import com.financia.system.service.MemberBusinessWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("memberBusinessWalletService")
public class MemberBusinessWalletServiceImpl extends ServiceImpl<MemberBusinessWalletMapper, MemberBusinessWallet> implements MemberBusinessWalletService {

    @Autowired
    private IMemberService memberService;

    @Autowired
    private MemberWalletNationalCurrencyMapper memberWalletNationalCurrencyMapper;

    @Autowired
    private MemberBusinessWalletMapper memberBusinessWalletMapper;

    @Autowired
    private PConversionRateMapper pConversionRateMapper;

    @Autowired
    private MemberBusinessWalletRecordService businessWalletRecordService;

    @Autowired
    private PCryptoCurrencyMapper pCryptoCurrencyMapper;

    @Autowired
    private RemoteQuantitativeService quantitativeService;

    @Autowired
    private RemoteSuperLeverageService remoteSuperLeverageService;

    @Autowired
    private RemoteSwapService remoteSwapService;



    /**
     * 更新余额（加余额）
     *
     * @param memberId
     * @param money
     * @return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public boolean updateAddBalance(Long memberId, BigDecimal money, String busId, BusinessSubType businessSubType, String mark) {
        MemberBusinessWallet wallet = getOne(new QueryWrapper<MemberBusinessWallet>()
                .lambda()
                .eq(MemberBusinessWallet::getStatus, DataStatus.VALID.getCode())
                .eq(MemberBusinessWallet::getMemberId, memberId)
                .eq(MemberBusinessWallet::getType, WalletType.BALANCE.getCode()));
        MemberBusinessWallet update = new MemberBusinessWallet();
        update.setId(wallet.getId());
        update.setMoney(wallet.getMoney().add(money));
        boolean b = updateById(update);
        if (b) {
            // 保存操作流水
            businessWalletRecordService.saveRecord(wallet.getId(), money, update.getMoney(), TradeType.ADD, busId, businessSubType, mark);
            return true;
        }
        return false;
    }

    /**
     * 更新余额（扣减余额）
     *
     * @param memberId
     * @param money
     * @return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public boolean updateSubBalance(Long memberId, BigDecimal money, String busId, BusinessSubType businessSubType, String mark) {
        MemberBusinessWallet wallet = getOne(new QueryWrapper<MemberBusinessWallet>()
                .lambda()
                .eq(MemberBusinessWallet::getStatus, DataStatus.VALID.getCode())
                .eq(MemberBusinessWallet::getMemberId, memberId)
                .eq(MemberBusinessWallet::getType, WalletType.BALANCE.getCode()));
        if (wallet.getMoney().compareTo(money) < 0) {
            return false;
        }
        baseMapper.updateSubBalance(memberId, money);
        MemberBusinessWallet byId = getById(wallet.getId());
        if (BigDecimal.ZERO.compareTo(byId.getMoney()) > 0) {
            throw new ServiceException("余额不足");
        }
        // 保存操作流水
        businessWalletRecordService.saveRecord(wallet.getId(), money, byId.getMoney(), TradeType.SUBTRACT, busId, businessSubType, mark);
        return true;
    }

    /**
     * 扣减冻结余额
     *
     * @param memberId
     * @param money
     * @return
     */
    public AjaxResult updateSubFreezeBalance(Long memberId, BigDecimal money, String busId, BusinessSubType businessSubType, String mark) {
        Member byId = memberService.getOne(new QueryWrapper<Member>()
                .lambda().eq(Member::getId, memberId)
                .ne(Member::getStatus, DataStatus.DELETED.getCode()));
        if (byId == null) {
            return AjaxResult.error("Membership does not exist");
        }
        if (byId.getStatus() == DataStatus.UNENABLE.getCode()) {
            return AjaxResult.error("Member is disabled");
        }
        if (byId.getTransactionStatus() == 0) {
            return AjaxResult.error("Members are barred from trading");
        }
        MemberBusinessWallet wallet = getOne(new QueryWrapper<MemberBusinessWallet>()
                .lambda()
                .eq(MemberBusinessWallet::getStatus, DataStatus.VALID.getCode())
                .eq(MemberBusinessWallet::getMemberId, memberId)
                .eq(MemberBusinessWallet::getType, WalletType.BALANCE.getCode()));
        if (wallet.getMoney().compareTo(money) == -1) {
            return AjaxResult.error("Lack of balance");
        }
        if (wallet.getFreezeMoney().subtract(money).compareTo(new BigDecimal(0l)) == -1) {
            return AjaxResult.error("Account data is abnormal.");
        }
        MemberBusinessWallet update = new MemberBusinessWallet();
        update.setId(wallet.getId());
        update.setFreezeMoney(wallet.getFreezeMoney().subtract(money));
        boolean b = updateById(update);
        if (b) {
            businessWalletRecordService.saveRecord(wallet.getId(), money, update.getMoney(), TradeType.FREEZE_SUBTRACT, busId, businessSubType, mark);
            return AjaxResult.success();
        }
        return AjaxResult.error("Transaction failure");
    }

    /**
     * 冻结余额
     *
     * @param memberId
     * @param money
     * @return
     */
    public AjaxResult updateFreezeBalance(Long memberId, BigDecimal money, String busId, BusinessSubType businessSubType, String mark) {
        Member byId = memberService.getOne(new QueryWrapper<Member>()
                .lambda().eq(Member::getId, memberId)
                .ne(Member::getStatus, DataStatus.DELETED.getCode()));
        if (byId == null) {
            return AjaxResult.error("Membership does not exist");
        }
        if (byId.getStatus() == DataStatus.UNENABLE.getCode()) {
            return AjaxResult.error("Member is disabled");
        }
        if (byId.getTransactionStatus() == 0) {
            return AjaxResult.error("Members are barred from trading");
        }
        MemberBusinessWallet wallet = getOne(new QueryWrapper<MemberBusinessWallet>()
                .lambda()
                .eq(MemberBusinessWallet::getStatus, DataStatus.VALID.getCode())
                .eq(MemberBusinessWallet::getMemberId, memberId)
                .eq(MemberBusinessWallet::getType, WalletType.BALANCE.getCode()));
        if (wallet.getMoney().compareTo(money) == -1) {
            return AjaxResult.error("Lack of balance");
        }
        MemberBusinessWallet update = new MemberBusinessWallet();
        update.setId(wallet.getId());
        update.setMoney(wallet.getMoney().subtract(money));
        update.setFreezeMoney(wallet.getFreezeMoney().add(money));
        boolean b = updateById(update);
        if (b) {
            // 保存操作流水
            businessWalletRecordService.saveRecord(wallet.getId(), money, update.getMoney(), TradeType.FREEZE, busId, businessSubType, mark);
            return AjaxResult.success();
        }
        return AjaxResult.error("Transaction failure");
    }

    @Override
    public boolean createWallet(Long memberId) {
        if (saveWallet(memberId, WalletType.BALANCE)) {
            return false;
        }
        return false;
    }

    private boolean saveWallet(Long memberId, WalletType balance) {
        MemberBusinessWallet balanceWallet = new MemberBusinessWallet();
        balanceWallet.setMemberId(memberId);
        balanceWallet.setMoney(new BigDecimal(0l));
        balanceWallet.setFreezeMoney(new BigDecimal(0l));
        balanceWallet.setStatus(DataStatus.VALID.getCode());
        balanceWallet.setType(balance.getCode());
        if (!save(balanceWallet)) {
            return true;
        }
        return false;
    }


    public Map<String, BigDecimal> getBalance(Long memberId) {

        Member member = memberService.getById(memberId);
        // 充值进来的USDT
        Map<String, BigDecimal> totalAssets = memberBusinessWalletMapper.getMoneyByMemberId(memberId, 1);
        String defaultLegal = member.getDefaultLegal();
        //沒有默认法币，默认为USD
        if (StringUtils.isEmpty(defaultLegal)) {
            defaultLegal = "USD";
        }
        PConversionRate pConversionRate = pConversionRateMapper.getByCurrencySymbol(defaultLegal);
        // 加个对错误法币的判断，如果没有对应的换汇数据，则默认为USD
        if (pConversionRate == null) {
            pConversionRate = pConversionRateMapper.getByCurrencySymbol("USD");
        }
        totalAssets.put("national_money", totalAssets.get("money").multiply(pConversionRate.getConversionRate()));

        return totalAssets;

    }

    @Override
    public List<Map> getFiaCurrencyList(Long memberId) {
        List<Map> fiaCurrencyList = memberWalletNationalCurrencyMapper.getFiaCurrencyList(memberId, 1);
        for (Map map : fiaCurrencyList) {
            map.put("available_money", new BigDecimal(String.valueOf(map.get("money"))).subtract(new BigDecimal(String.valueOf(map.get("freeze_money")))));
        }
        return fiaCurrencyList;
    }


    @Override
    public MemberBusinessWallet getMemberBusinessWalletByMemberId(Long memberId) {
        MemberBusinessWallet wallet = getOne(new QueryWrapper<MemberBusinessWallet>()
                .lambda()
                .eq(MemberBusinessWallet::getMemberId, memberId)
                .eq(MemberBusinessWallet::getStatus, 1));
        return wallet;
    }

    @Override
    public BigDecimal convertFiatCurrency(Long memberId, BigDecimal amount) {
        Member member = memberService.getById(memberId);
        String defaultLegal = member.getDefaultLegal();
        //沒有默认法币，默认为USD
        if (StringUtils.isEmpty(defaultLegal)) {
            defaultLegal = "USD";
        }
        PConversionRate pConversionRate = pConversionRateMapper.getByCurrencySymbol(defaultLegal);
        // 加个对错误法币的判断，如果没有对应的换汇数据，则默认为USD
        if (pConversionRate == null) {
            pConversionRate = pConversionRateMapper.getByCurrencySymbol("USD");
        }
        return amount.multiply(pConversionRate.getConversionRate());

    }

    @Override
    public List<Map> getTransferCurrencyDetail(Long memberId) {
        // 会员可转账数字货币列表，此处要显示余额
        // todo 第一版本只支持usdt,暂时不从ex_coin去查询转账配置，默认写死USDT
        //  List<String>  coinName = exCoinMapper.getCoinNameByCanRecharge(1);
        List<Map> list = new ArrayList<>();
        String coinName = "USDT";
        Map currencyDetail = pCryptoCurrencyMapper.getByCoinName(coinName);
        MemberBusinessWallet memberBusinessWallet = getMemberBusinessWalletByMemberId(memberId);
        // toPlainString避免出现科学记数法
        currencyDetail.put("money", memberBusinessWallet.getMoney().toPlainString());
        currencyDetail.put("freeze_money", memberBusinessWallet.getFreezeMoney().toPlainString());
        list.add(currencyDetail);

        return list;
    }

    @Override
    public Map<String, Map<String, BigDecimal>> getTotalAssets(Long memberId) {
        Member member = memberService.getById(memberId);
        // 余额
        MemberBusinessWallet wallet = getOne(new QueryWrapper<MemberBusinessWallet>()
                .lambda()
                .eq(MemberBusinessWallet::getMemberId, memberId)
                .eq(MemberBusinessWallet::getStatus, 1));

        //量化理财
        BigDecimal quantitative = quantitativeService.positionFeign(memberId);
        //合约
        BigDecimal contract = remoteSwapService.contractWalletTotal(memberId);
        //杠杆
        BigDecimal superLeverage = remoteSuperLeverageService.superWalletTotal(memberId);
        // 股票
        BigDecimal stock = BigDecimal.ZERO;

        Map<String, Map<String, BigDecimal>> totalAssets = new HashMap<>();

        Map<String, BigDecimal> totalAssetMap = new HashMap<>();
        Map<String, BigDecimal> balanceMap = new HashMap<>();
        Map<String, BigDecimal> quantitativeMap = new HashMap<>();
        Map<String, BigDecimal> contractMap = new HashMap<>();
        Map<String, BigDecimal> superLeverageMap = new HashMap<>();
        Map<String, BigDecimal> stockMap = new HashMap<>();


        String defaultLegal = member.getDefaultLegal();
        //沒有默认法币，默认为USD
        if (StringUtils.isEmpty(defaultLegal)) {
            defaultLegal = "USD";
        }
        PConversionRate pConversionRate = pConversionRateMapper.getByCurrencySymbol(defaultLegal);
        // 加个对错误法币的判断，如果没有对应的换汇数据，则默认为USD
        if (pConversionRate == null) {
            pConversionRate = pConversionRateMapper.getByCurrencySymbol("USD");
        }
        balanceMap.put("balance", wallet.getMoney().add(wallet.getFreezeMoney()));
        balanceMap.put("freezeMoney", wallet.getFreezeMoney());
        balanceMap.put("balanceFiat", (wallet.getMoney().add(wallet.getFreezeMoney())).multiply(pConversionRate.getConversionRate()));

        quantitativeMap.put("balance", quantitative);
        quantitativeMap.put("balanceFiat", quantitative.multiply(pConversionRate.getConversionRate()));

        contractMap.put("balance", contract);
        contractMap.put("balanceFiat", contract.multiply(pConversionRate.getConversionRate()));

        superLeverageMap.put("balance", superLeverage);
        superLeverageMap.put("balanceFiat", superLeverage.multiply(pConversionRate.getConversionRate()));


        stockMap.put("balance", stock);
        stockMap.put("balanceFiat", stock.multiply(pConversionRate.getConversionRate()));

        BigDecimal total = wallet.getMoney().add(wallet.getFreezeMoney()).add(quantitative).add(superLeverage).add(contract);
        totalAssetMap.put("balance", total);
        totalAssetMap.put("balanceFiat", total.multiply(pConversionRate.getConversionRate()));

        totalAssets.put("totalAssets", totalAssetMap);
        totalAssets.put("balance", balanceMap);
        totalAssets.put("quantitative", quantitativeMap);
        totalAssets.put("contract", contractMap);
        totalAssets.put("superLeverage", superLeverageMap);
        totalAssets.put("stock", stockMap);



        return totalAssets;
    }
}
