package com.switchfully.eurder.order.domain;


import com.switchfully.eurder.item.domain.Item;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "ORDER_DETAIL")
public class OrderDetail {
    @Id
    @SequenceGenerator(name = "order_detail_seq", sequenceName = "order_detail_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_detail_seq")
    private Long orderDetailId;
    @ManyToOne
    @JoinColumn(name = "FK_ITEM_ID")
    private Item item;
    @Column(name = "AMOUNT")
    private int amount;
    @Column(name = "SHIPPING_DATE", columnDefinition = "TIMESTAMP")
    private LocalDate shippingDate;
    @Column(name = "PRICE_OF_ORDER_DETAIL")
    private double priceOfOrderDetail;
    @ManyToOne
    @JoinColumn(name = "FK_ORDER_HEADER_ID")
    private OrderHeader orderHeader;

    public OrderDetail(Item item, int amount, OrderHeader orderHeader) {
        this.item = item;
        this.amount = amount;
        this.shippingDate = LocalDate.now();
        this.priceOfOrderDetail = 0;
        this.orderHeader = orderHeader;
    }

    public OrderDetail() {
    }

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public double getPriceOfOrderDetail() {
        return priceOfOrderDetail;
    }

    public OrderHeader getOrderHeader() {
        return orderHeader;
    }

    public void setShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
    }

    public void setOrderHeader(OrderHeader orderHeader) {
        this.orderHeader = orderHeader;
    }

    public void setPriceOfOrderDetail(double priceOfOrderDetail) {
        this.priceOfOrderDetail = priceOfOrderDetail;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setItem(Item item) {
        this.item = item;
    }



}
