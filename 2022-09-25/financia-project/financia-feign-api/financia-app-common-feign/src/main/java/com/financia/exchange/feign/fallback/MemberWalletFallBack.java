package com.financia.exchange.feign.fallback;

import com.financia.common.core.enums.BusinessSubType;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.MemberWalletNationalCurrency;
import com.financia.exchange.feign.client.RemoteMemberWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 喜欢看客户端feign接口快速失败
 * @author: zealon
 * @since: 2019/7/4
 */
@Component
public class MemberWalletFallBack implements FallbackFactory<RemoteMemberWalletService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberWalletFallBack.class);

    @Override
    public RemoteMemberWalletService create(Throwable cause) {
        return new RemoteMemberWalletService() {
            @Override
            public Boolean updateAddBalance (Long memberId, BigDecimal money, String busId,BusinessSubType type,String mark) {
                LOGGER.info("updateAddBalance失败。。。。。。。。。。。。。。。。");
                LOGGER.info(cause.getMessage());
                return false;
            }
            @Override
            public Boolean updateSubBalance (Long memberId,  BigDecimal money, String busId,BusinessSubType type,String mark) {
                LOGGER.info("updateSubBalance。。。。。。。。。。。。。。。。");
                LOGGER.info(cause.getMessage());
                return false;
            }

            @Override
            public MemberBusinessWallet getMemberBusinessWalletByMemberId(Long memberId) {
                LOGGER.info("getBalance。。。。。。。。。。。。。。。。");
                LOGGER.info(cause.getMessage());
                return null;
            }
            @Override
            public BigDecimal convertFiatCurrencyFeign(BigDecimal amount,Long userId) {
                LOGGER.info("convertFiatCurrency。。。。。。。。。。。。。。。。");
                LOGGER.info(cause.getMessage());
                return null;
            }
            @Override
            public MemberWalletNationalCurrency getNationalCurrencyByMemberIdAndCoinId(@RequestBody MemberWalletNationalCurrency memberWalletNationalCurrency) {
                LOGGER.info("getNationalCurrencyByMemberIdAndCoinId。。。。。。。。。。。。。。。。");
                LOGGER.info(cause.getMessage());
                return null;
            }
            @Override
            public int addMoney (@RequestBody MemberWalletNationalCurrency memberWalletNationalCurrency) {
                LOGGER.info("addMoney。。。。。。。。。。。。。。。。");
                LOGGER.info(cause.getMessage());
                return 0;
            }
            @Override
            public int subMoney(@RequestBody MemberWalletNationalCurrency memberWalletNationalCurrency) {
                LOGGER.info("subMoney。。。。。。。。。。。。。。。。");
                LOGGER.info(cause.getMessage());
                return 0;
            }
            @Override
            public int addNationalCurrency(@RequestBody MemberWalletNationalCurrency memberWalletNationalCurrency) {
                LOGGER.info("addNationalCurrency。。。。。。。。。。。。。。。。");
                LOGGER.info(cause.getMessage());
                return 0;
            }
            @Override
            public Map verifyJyPassword(@RequestParam("memberId") Long memberId,@RequestParam("jyPassword") String jyPassword) {
                LOGGER.info("verifyJyPassword。。。。。。。。。。。。。。。。");
                LOGGER.info(cause.getMessage());
                return null;
            }
        };
    }
}
