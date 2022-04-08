package com.switchfully.eurder.order.service;

import com.switchfully.eurder.customer.exception.CustomerNotFoundException;
import com.switchfully.eurder.customer.service.CustomerService;
import com.switchfully.eurder.item.domain.Item;
import com.switchfully.eurder.item.service.ItemService;
import com.switchfully.eurder.order.api.dto.CreateGroupItemDto;
import com.switchfully.eurder.order.api.dto.CreateOrderDto;
import com.switchfully.eurder.order.domain.GroupItem;
import com.switchfully.eurder.order.domain.Order;
import com.switchfully.eurder.order.domain.OrderReport;
import com.switchfully.eurder.order.domain.OrderRepository;
import com.switchfully.eurder.order.exception.EmptyInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {
    private final Logger serviceLogger = LoggerFactory.getLogger(CustomerService.class);
    public static final int DAYS_OF_SHIPPING_IN_STOCK = 1;
    public static final int DAYS_OF_SHIPPING_OUT_OF_STOCK = 7;
    private final OrderRepository orderRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CustomerService customerService;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public Order saveNewOrder(CreateOrderDto createOrderDTO) {

        loggingError(createOrderDTO.getItemGroup());
        loggingError(createOrderDTO.getCustomerId());
        Order order = orderMapper.toOrder(createOrderDTO);

        order.setTotalPrice(calculateTotalPrice(order));
        customerService.getCustomerById(order.getCustomerId());
        calculateShippingDate(order);
        editStockOfItem(order);

        orderRepository.save(order);
        return order;
    }

    public double calculateTotalPrice(Order order) {
        List<GroupItem> groupItems = order.getItemGroup();
        double totalPrice = 0;
        for (GroupItem groupItem : groupItems) {
            Item item = itemService.findItemById(groupItem.getItemId());
            totalPrice += groupItem.getAmount() * item.getPrice();
        }
        return totalPrice;
    }

    public void calculateShippingDate(Order order) {
        List<GroupItem> groupItems = order.getItemGroup();
        for (GroupItem groupItem : groupItems) {
            Item item = itemService.findItemById(groupItem.getItemId());
            if (item.getAmount() > groupItem.getAmount()) {
                groupItem.setShippingDate(LocalDate.now().plusDays(DAYS_OF_SHIPPING_IN_STOCK));
            } else {
                groupItem.setShippingDate(LocalDate.now().plusDays(DAYS_OF_SHIPPING_OUT_OF_STOCK));
            }
        }
    }

    public void editStockOfItem(Order order) {
        List<GroupItem> groupItems = order.getItemGroup();
        for (GroupItem groupItem : groupItems) {
            Item item = itemService.findItemById(groupItem.getItemId());
            itemService.removeFromStock(item, groupItem.getAmount());
        }
    }

    private void loggingError(String input) {
        if (input.isEmpty()) {
            serviceLogger.error("customer" + " is empty!");
            throw new EmptyInputException("customer");
        }
    }

    private void loggingError(List<CreateGroupItemDto> input) {
        if (input.isEmpty()) {
            serviceLogger.error("GroupItemList" + " is empty!");
            throw new EmptyInputException("GroupItemList");
        }
    }

    public OrderReport getOrderReport(String customerId) {
        if(!customerService.checkIfCustomerExists(customerId)){
            serviceLogger.error("Customer with customer ID " + customerId + " not found");
            throw new CustomerNotFoundException(customerId);
        }
        List<Order> orderList =
                orderRepository.getAllOrders().stream()
                        .filter(order -> order.getCustomerId().equals(customerId)).toList();
        double totalPrice =orderList.stream().mapToDouble(Order::getTotalPrice).sum();

        return new OrderReport(orderMapper.toOrderDto(orderList),totalPrice);
    }
}
