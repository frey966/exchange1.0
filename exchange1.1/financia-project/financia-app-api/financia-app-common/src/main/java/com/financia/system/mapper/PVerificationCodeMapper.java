package com.financia.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.common.PVerificationCode;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户注册或登录发送邮箱验证码
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-07-14 17:50:27
 */
@Mapper
public interface PVerificationCodeMapper extends BaseMapper<PVerificationCode> {

}
