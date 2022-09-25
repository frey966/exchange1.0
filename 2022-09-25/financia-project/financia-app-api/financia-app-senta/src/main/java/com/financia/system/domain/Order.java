package com.financia.system.domain;

public class Order {


    private Integer id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 订单状态
     */
    private int status;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * 总金额
     */
    private Double totalPrice;

    public Order()
    {
    }

    public Order(Long userId, Long productId, int status, Integer amount)
    {
        this.userId = userId;
        this.productId = productId;
        this.status = status;
        this.amount = amount;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public Integer getAmount()
    {
        return amount;
    }

    public void setAmount(Integer amount)
    {
        this.amount = amount;
    }

    public Double getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice)
    {
        this.totalPrice = totalPrice;
    }

}
