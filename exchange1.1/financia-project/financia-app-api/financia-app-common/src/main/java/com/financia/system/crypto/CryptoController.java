package com.financia.system.crypto;

import com.financia.system.crypto.bean.CryptoTransaction;
import com.financia.system.crypto.bean.MessageResult;
import com.financia.system.crypto.service.ICoinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品充值逻辑。
 *
 * 预先定义一个job，job中放一个list，list中放需要查询余额的地址。
 * job 1s或者是10s执行一次，循环查询list中地址的余额。并且在记录每一个地址从开始查询到目前的时间和次数。
 * 如果查到某个地址有余额，则将该地址的钱转移到主地址中。并且给对应客户的账户增加usdt余额。并且将该地址从list中移除。
 * 如果某个地址连续10分钟也没有查询到有余额，则将该地址从list中移除。
 *
 * 客户在h5界面点击充值成功之后，后台需要将该客户的地址放在 job的list中循环查询。
 * 查询的逻辑如上所示。
 *
 * */


@RestController
@RequestMapping("crypto")
@Api(tags = "新接口-交易功能")
public class CryptoController {
    @RequestMapping(value = "crypto/getBalance",method = RequestMethod.POST)
    @ApiOperation(value = "查询账户USDT 余额")
    public MessageResult getBalance(@RequestParam("address") String address, @RequestParam("chain") int chain){

        ICoinService coinService=Manager.getInstance().getService(chain);

        if(coinService==null){
            return MessageResult.error("No such chain: "+chain);
        }

        BigDecimal balance=coinService.getBalance(address);

        return MessageResult.success(balance.toString());
    }

    @RequestMapping(value = "crypto/transfer",method = RequestMethod.POST)
    @ApiOperation(value = "")
    public MessageResult transfer(@RequestParam("from") String from,@RequestParam("to") String to, @RequestParam("private_key") String privateKey,@RequestParam("value") BigDecimal value,@RequestParam("chain")int chain){
        ICoinService coinService=Manager.getInstance().getService(chain);
        if(coinService==null){
            return MessageResult.error("No such chain: "+chain);
        }

        String hex= coinService.transfer(from,privateKey,to,coinService.transferUsdt2BigInteger(value));

        return MessageResult.success(hex);
    }

    @RequestMapping(value = "crypto/getList",method = RequestMethod.POST)
    @ApiOperation(value = "获取usdt最近的交易记录")
    public MessageResult getList(@RequestParam("address") String address,@RequestParam("chain")int chain){
        ICoinService coinService=Manager.getInstance().getService(chain);
        if(coinService==null){
            return MessageResult.error("No such chain: "+chain);
        }

        List<CryptoTransaction> res= coinService.getLastTransactions(address,50);

        return MessageResult.success("success",res);
    }
}
