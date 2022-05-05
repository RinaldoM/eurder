package com.switchfully.eurder.order.domain;

import com.switchfully.eurder.customer.domain.Customer;

import javax.persistence.*;

@Entity
@Table(name = "ORDER_HEADER")
public class OrderHeader {
    @Id
    @SequenceGenerator(name = "order_header_seq", sequenceName = "order_header_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_header_seq")
    private Long orderHeaderId;
    @ManyToOne
    @JoinColumn(name = "FK_CUSTOMER_ID")
    private Customer customer;
    @Column(name = "TOTAL_PRICE")
    private double totalPrice;

    public OrderHeader() {
    }

    public OrderHeader(Customer customerId) {
        this.customer = customerId;
        this.totalPrice = 0;
    }

    public Long getOrderHeaderId() {
        return orderHeaderId;
    }


    public Customer getCustomer() {
        return customer;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCustomer(Customer customerId) {
        this.customer = customerId;
    }
}