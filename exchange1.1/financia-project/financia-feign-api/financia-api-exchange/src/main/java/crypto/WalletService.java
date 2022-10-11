package crypto;

import com.financia.common.core.constant.SecurityConstants;
import com.financia.common.core.constant.ServiceNameConstants;
import com.financia.common.core.domain.R;
import crypto.factory.WalletFallbackFactory;
import crypto.model.WalletBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务
 * 
 * @author ruoyi
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = WalletFallbackFactory.class)
public interface WalletService
{
    /**
     * 通过用户名查询用户信息
     *
     * @param type 用户名
     * @param inner 请求来源
     * @return 结果
     */
    @GetMapping("/user/info/{username}")
    public R<WalletBean> getUserInfo(@PathVariable("type") String type, @RequestHeader(SecurityConstants.INNER) String inner);

    /**
     * 注册用户信息
     *
     * @param sysUser 用户信息
     * @param inner 请求来源
     * @return 结果
     */
    @PostMapping("/user/register")
    public R<Boolean> registerUserInfo(@RequestBody WalletBean sysUser, @RequestHeader(SecurityConstants.INNER) String inner);

}
