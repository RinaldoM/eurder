package com.switchfully.eurder.order.service;

import com.switchfully.eurder.item.domain.Item;
import com.switchfully.eurder.item.service.ItemService;
import com.switchfully.eurder.order.domain.GroupItem;
import com.switchfully.eurder.order.domain.Order;
import com.switchfully.eurder.order.domain.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {
    public static final int DAYS_OF_SHIPPING_IN_STOCK = 1;
    public static final int DAYS_OF_SHIPPING_OUT_OF_STOCK = 7;
    private final OrderRepository orderRepository;
    @Autowired
    private ItemService itemService;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order saveNewOrder(Order order) {
        order.setTotalPrice(calculateTotalPrice(order));
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

}
