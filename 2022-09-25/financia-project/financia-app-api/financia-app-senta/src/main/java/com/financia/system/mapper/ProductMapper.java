package com.financia.system.mapper;

import com.financia.system.domain.Product;

public interface ProductMapper {


    public Product selectById(Long productId);

    public Integer updateById(Product product);

}
