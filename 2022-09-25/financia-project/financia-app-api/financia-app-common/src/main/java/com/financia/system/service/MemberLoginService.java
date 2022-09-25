package com.financia.system.service;

import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.Member;

public interface MemberLoginService {
    AjaxResult login(Member member);

    AjaxResult LoginByCode(String code, long memberId);

    AjaxResult LoginByPassword(String ip, String userName, String password);
    AjaxResult LoginByGesturePassword(String ip, Long id, String password);
    AjaxResult gsturePasswordSave(Long id, String password);

    AjaxResult gestureSwitch(Long id , Integer isOpent);
    AjaxResult closePasswordVerify(Long id, String password, String loginPassword);

    boolean verifyJyPassword (Long memberId,String jyPwd);

    boolean verifyLoginPassword (Long memberId,String loginPwd);

    Integer verifyJyPasswordFailureCount(Long memberId);
}
