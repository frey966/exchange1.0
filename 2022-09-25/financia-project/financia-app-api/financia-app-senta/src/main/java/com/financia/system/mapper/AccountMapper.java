package com.financia.system.mapper;

import com.financia.system.domain.Account;

public interface AccountMapper {

    public Account selectById(Long userId);

    public Integer updateById(Account account);

}
