package com.financia.common.core.constant;

import java.math.BigDecimal;

public class ExchangeConstants {
    /**
     * 绑定邮箱前缀
     */
    public static final String EMAIL_BIND_CODE_PREFIX = "EMAIL_BIND_CODE_";

    /**
     * 提现手续费率
     */
    public static final BigDecimal WITHDRAWAL_PROCEDURE_RATE =  new BigDecimal("0.001");
    /**
     * 提现手续费最小值
     */
    public static final BigDecimal WITHDRAWAL_PROCEDURE_MINI =  new BigDecimal("1");
}
