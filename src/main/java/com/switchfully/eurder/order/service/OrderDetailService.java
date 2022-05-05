package com.switchfully.eurder.order.service;

import com.switchfully.eurder.customer.service.CustomerService;
import com.switchfully.eurder.item.domain.Item;
import com.switchfully.eurder.item.exceptions.InvalidNumberInputException;
import com.switchfully.eurder.item.service.ItemService;
import com.switchfully.eurder.order.api.dto.CreateOrderDetailDto;
import com.switchfully.eurder.order.api.dto.OrderDetailDto;
import com.switchfully.eurder.order.domain.OrderDetail;
import com.switchfully.eurder.order.domain.OrderDetailRepository;
import com.switchfully.eurder.order.domain.OrderHeader;
import com.switchfully.eurder.order.exception.EmptyInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailService {
    public static final int DAYS_OF_SHIPPING_IN_STOCK = 1;
    public static final int DAYS_OF_SHIPPING_OUT_OF_STOCK = 7;
    private final Logger serviceLogger = LoggerFactory.getLogger(OrderDetailService.class);

    private final OrderDetailRepository orderDetailRepository;
    private final ItemService itemService;
    private final OrderDetailMapper orderDetailMapper;

    public OrderDetailService(OrderDetailRepository orderDetailRepository, ItemService itemService, OrderDetailMapper orderDetailMapper) {
        this.orderDetailRepository = orderDetailRepository;
        this.itemService = itemService;
        this.orderDetailMapper = orderDetailMapper;
    }

    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    public List<OrderDetail> getOrderDetailsFromOrderId(Long orderId) {
        return orderDetailRepository.findAll()
                .stream()
                .filter(orderDetail -> orderDetail.getOrderHeader().getOrderHeaderId() == orderId)
                .collect(Collectors.toList());
    }

    public OrderDetailDto saveOrderDetail(CreateOrderDetailDto createOrderDetailDto, OrderHeader orderHeader) {
        OrderDetail orderDetail = new OrderDetail();
        loggingError(createOrderDetailDto.getAmount());
        Item itemById = itemService.findItemById(createOrderDetailDto.getItemId());
        orderDetail.setItem(itemById);
        double orderLinePrice = calculatePriceOrderDetail(createOrderDetailDto, itemById.getPrice());
        orderHeader.setTotalPrice(orderHeader.getTotalPrice() + orderLinePrice);
        orderDetail.setOrderHeader(orderHeader);
        orderDetail.setAmount(createOrderDetailDto.getAmount());
        orderDetail.setPriceOfOrderDetail(orderLinePrice);
        orderDetail.setShippingDate(calculateShippingDate(orderDetail));
        editStockOfItem(orderDetail);
        orderDetailRepository.save(orderDetail);
        return orderDetailMapper.toDto(orderDetail);
    }

    private double calculatePriceOrderDetail(CreateOrderDetailDto createOrderDetailDto, double itemPrice) {
        return createOrderDetailDto.getAmount() * itemPrice;
    }

    private void loggingError(double input) {
        if (input<=0) {
            serviceLogger.error("Amount" + " is equal or smaller than 0!");
            throw new InvalidNumberInputException(String.valueOf(input));
        }
    }

    //    public double calculateTotalPrice(OrderHeader order) {
//        List<OrderDetail> orderDetails = getOrderDetailsFromOrderId(order.getOrderHeaderId());
//        double totalPrice = 0;
//        for (OrderDetail orderDetail : orderDetails) {
//            Item item = itemService.findItemById(orderDetail.getItemId());
//            totalPrice += orderDetail.getAmount() * item.getPrice();
//        }
//
//        return totalPrice;
//    }
//
    public LocalDate calculateShippingDate(OrderDetail orderDetail) {
        List<OrderDetail> orderDetails = getOrderDetailsFromOrderId(orderDetail.getOrderHeader().getOrderHeaderId());
            Item item = orderDetail.getItem();
            if (item.getAmount() > orderDetail.getAmount()) {
                return LocalDate.now().plusDays(DAYS_OF_SHIPPING_IN_STOCK);
            } else {
                return LocalDate.now().plusDays(DAYS_OF_SHIPPING_OUT_OF_STOCK);
            }
    }
    public OrderDetail getOrderDetailById(Long id){
        return orderDetailRepository.getById(id);
    }

    public void editStockOfItem(OrderDetail orderDetail) {
            Item item = orderDetail.getItem();
            itemService.removeFromStock(item, orderDetail.getAmount());
    }
}
