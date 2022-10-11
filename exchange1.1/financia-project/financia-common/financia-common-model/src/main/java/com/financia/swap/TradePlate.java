package com.financia.swap;


import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.LinkedList;

/**
 * 盘口信息
 */
@Data
@Slf4j
public class TradePlate {
    private LinkedList<TradePlateItem> items;
    //最大深度
    private int maxDepth = 100;
    //方向
    private ExchangeOrderDirection direction;
    private String symbol;
    public TradePlate(){

    }

    public TradePlate(String symbol, ExchangeOrderDirection direction) {
        this.direction = direction;
        this.symbol = symbol;
        items = new LinkedList<>();
    }

    public void setItems(LinkedList<TradePlateItem> items){
        this.items = items;
    }

    public BigDecimal getHighestPrice(){
        if(items.size() == 0) {
            return BigDecimal.ZERO;
        }
        if(direction == ExchangeOrderDirection.BUY){
            return items.getFirst().getPrice();
        }
        else{
            return items.getLast().getPrice();
        }
    }

    public int getDepth(){
        return items.size();
    }


    public BigDecimal getLowestPrice(){
        if(items.size() == 0) {
            return BigDecimal.ZERO;
        }
        if(direction == ExchangeOrderDirection.BUY){
            return items.getLast().getPrice();
        }
        else{
            return items.getFirst().getPrice();
        }
    }

    /**
     * 获取委托量最大的档位
     * @return
     */
    public BigDecimal getMaxAmount(){
        if(items.size() == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal amount = BigDecimal.ZERO;
        for(TradePlateItem item:items){
            if(item.getAmount().compareTo(amount)>0){
                amount = item.getAmount();
            }
        }
        return amount;
    }

    /**
     * 获取委托量最小的档位
     * @return
     */
    public BigDecimal getMinAmount(){
        if(items.size() == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal amount = items.getFirst().getAmount();
        for(TradePlateItem item:items){
            if(item.getAmount().compareTo(amount) < 0){
                amount = item.getAmount();
            }
        }
        return amount;
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        json.put("direction",direction);
        json.put("maxAmount",getMaxAmount());
        json.put("minAmount",getMinAmount());
        json.put("highestPrice",getHighestPrice());
        json.put("lowestPrice",getLowestPrice());
        json.put("symbol",getSymbol());
        json.put("items",items);
        return json;
    }

    public JSONObject toJSON(int limit){
        JSONObject json = new JSONObject();
        json.put("direction",direction);
        json.put("maxAmount",getMaxAmount());
        json.put("minAmount",getMinAmount());
        json.put("highestPrice",getHighestPrice());
        json.put("lowestPrice",getLowestPrice());
        json.put("symbol",getSymbol());
        json.put("items",items.size() > limit ? items.subList(0,limit) : items);
        return json;
    }
}
