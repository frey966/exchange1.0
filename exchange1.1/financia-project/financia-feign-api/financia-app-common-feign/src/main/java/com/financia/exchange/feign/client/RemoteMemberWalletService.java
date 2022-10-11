package com.financia.exchange.feign.client;

import com.financia.common.core.enums.BusinessSubType;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.MemberWalletNationalCurrency;
import com.financia.exchange.feign.fallback.MemberWalletFallBack;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;


/**
 * 钱包客户端feign接口
 * @author: zealon
 * @since: 2019/7/4
 */
@FeignClient(contextId = "remoteMemberWalletService", value = "financia-app-common", fallbackFactory = MemberWalletFallBack.class)
//@FeignClient(contextId = "remoteMemberWalletService", value = "127.0.0.1:9104", fallbackFactory = MemberWalletFallBack.class)
public interface RemoteMemberWalletService {

    /**
     * 增加钱包余额和交易记录
     * @param memberId
     * @param money
     * @return
     */
    @PostMapping("/member/wallet/updateAddBalance")
    Boolean updateAddBalance(@RequestParam("memberId") Long memberId, @RequestParam("money")  BigDecimal money, @RequestParam("busId")String busId, @RequestParam("businessSubType") BusinessSubType businessSubType, @RequestParam("mark")String mark);

    /**
     * 减少钱包余额和交易记录
     * @param memberId
     * @param money
     * @return
     */
    @PostMapping("/member/wallet/updateSubBalance")
    Boolean updateSubBalance(@RequestParam("memberId") Long memberId, @RequestParam("money")  BigDecimal money,@RequestParam("busId")String busId, @RequestParam("businessSubType") BusinessSubType businessSubType, @RequestParam("mark")String mark);

    /**
     * 获取钱包余额
     * @param memberId
     * @return
     */
    @PostMapping("/member/wallet/getMemberBusinessWalletByMemberId")
    MemberBusinessWallet getMemberBusinessWalletByMemberId(@RequestParam("memberId") Long memberId);


    @GetMapping("/member/wallet/convertFiatCurrencyFeign")
    BigDecimal convertFiatCurrencyFeign(@RequestParam("amount") BigDecimal amount, @RequestHeader("user_id") Long userId);

    /**
     * 根据会员id和法币id查询法币钱包
     * @param memberWalletNationalCurrency
     * @return
     */
    @PostMapping("/member/walletNationalCurrency/getNationalCurrencyByMemberIdAndCoinId")
    MemberWalletNationalCurrency getNationalCurrencyByMemberIdAndCoinId(@RequestBody MemberWalletNationalCurrency memberWalletNationalCurrency);

    /**
     * 根据会员id和法币id查询
     * 增加法币钱包
     * @param memberWalletNationalCurrency
     * @return
     */
    @PostMapping("/member/walletNationalCurrency/addMoney")
    int addMoney(@RequestBody MemberWalletNationalCurrency memberWalletNationalCurrency);

    /**
     * 根据会员id和法币id查询
     * 减少法币钱包
     * @param memberWalletNationalCurrency
     * @return
     */
    @PostMapping("/member/walletNationalCurrency/subMoney")
    int subMoney(@RequestBody MemberWalletNationalCurrency memberWalletNationalCurrency);

    /**
     * 添加会员法币钱包
     * @param memberWalletNationalCurrency
     * @return
     */
    @PostMapping("/member/walletNationalCurrency/addNationalCurrency")
    int addNationalCurrency(@RequestBody MemberWalletNationalCurrency memberWalletNationalCurrency);

    /**
     * 验证交易密码
     */
    @PostMapping("/member/walletNationalCurrency/verifyJyPassword")
    @ApiOperation(value = "验证交易密码", notes = "验证交易密码")
    Map verifyJyPassword(@RequestParam("memberId") Long memberId,@RequestParam("jyPassword") String jyPassword);
}
