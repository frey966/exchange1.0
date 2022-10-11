package com.financia.exchange.cluster.mongodb;

import lombok.Data;

import java.util.List;

@Data
public class Pagination<T> {

  public Pagination() {
  }

  public Pagination(Long currentPage, Long totalPage, Long totalNumber) {
    this.currentPage = currentPage;
    this.totalPage = totalPage;
    this.totalNumber = totalNumber;
  }

  /**
   * 每页显示条数
   */
  private Long pageSize = 8l;

  /**
   * 当前页
   */
  private Long currentPage = 1l;

  /**
   * 总页数
   */
  private Long totalPage = 1l;

  /**
   * 查询到的总数据量
   */
  private Long totalNumber = 0l;

  /**
   * 数据集
   */
  private List items;

  public Long getPageSize() {

    return pageSize;
  }

  /**
   * 处理查询后的结果数据
   *
   * @param items
   *      查询结果集
   *      总数
   */
  public void build(List items) {
    this.setItems(items);
    Long count = this.getTotalNumber();
    Long divisor = count / this.getPageSize();
    Long remainder = count % this.getPageSize();
    this.setTotalPage(remainder == 0 ? divisor == 0 ? 1 : divisor : divisor + 1);
  }

}
