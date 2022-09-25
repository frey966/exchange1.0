package com.financia.system.domain;

import java.util.Date;

public class Account {

    private Long id;

    /**
     * 余额
     */
    private Double balance;

    private Date lastUpdateTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Double getBalance()
    {
        return balance;
    }

    public void setBalance(Double balance)
    {
        this.balance = balance;
    }

    public Date getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }

}
