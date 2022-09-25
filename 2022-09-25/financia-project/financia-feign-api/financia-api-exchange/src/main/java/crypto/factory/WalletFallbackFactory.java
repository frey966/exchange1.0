package crypto.factory;

import com.financia.common.core.domain.R;
import crypto.WalletService;
import crypto.model.WalletBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 文件服务降级处理
 * 
 * @author ruoyi
 */
@Component
public class WalletFallbackFactory implements FallbackFactory<WalletService>
{
    private static final Logger log = LoggerFactory.getLogger(WalletFallbackFactory.class);

    @Override
    public WalletService create(Throwable throwable)
    {
        log.error("文件服务调用失败:{}", throwable.getMessage());
        return new WalletService()
        {
            @Override
            public R<WalletBean> getUserInfo(String username, String source)
            {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> registerUserInfo(WalletBean sysUser, String source)
            {
                return R.fail("注册用户失败:" + throwable.getMessage());
            }
        };
    }
}
