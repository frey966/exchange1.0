package com.financia.system.service;


import com.financia.common.core.enums.VerificationCodeType;

public interface VerificationCodeService {
    boolean emailSendCode(String email, VerificationCodeType type);
    boolean phoneSendCode(String email, VerificationCodeType type);
    boolean verify (String verificationCode, VerificationCodeType codeType, String suffix);
}
