package com.financia.system.mapper;

import com.financia.system.domain.Order;

public interface OrderMapper {


    public  Integer insert(Order order);

    public void updateById(Order order);

}
