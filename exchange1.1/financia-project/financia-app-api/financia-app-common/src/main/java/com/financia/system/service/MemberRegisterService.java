package com.financia.system.service;

import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.Member;

public interface MemberRegisterService {
    AjaxResult registerByEmail (Member member);
    AjaxResult registerByPhone (Member member);
    AjaxResult registerByUserName (Member member);
    boolean userExist(String email);
//    boolean phoneExist(String phone);
//    boolean userNameExist(String phone);
    boolean changePassword (Long memberId,String uid, String password);
}
