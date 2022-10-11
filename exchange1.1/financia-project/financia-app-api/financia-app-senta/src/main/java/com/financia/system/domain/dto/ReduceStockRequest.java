package com.financia.system.domain.dto;

public class ReduceStockRequest {

    private Long productId;

    private Integer amount;

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Integer getAmount()
    {
        return amount;
    }

    public void setAmount(Integer amount)
    {
        this.amount = amount;
    }

}
