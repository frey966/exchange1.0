package com.financia.quantitative.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CalculateScheduled {

    @Autowired
    private FinanceInterestMemberServiceImpl interestMemberService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void interestCalculated() {
        interestMemberService.interestCalculated();
    }
}
