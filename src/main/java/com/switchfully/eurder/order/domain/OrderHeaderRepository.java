package com.switchfully.eurder.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Long> {
    boolean existsByOrderHeaderId(Long id);

//    private final Logger repositoryLogger = LoggerFactory.getLogger(OrderRepository.class);
//    private final Map<String, OrderHeader> ordersById;
//
//    public OrderRepository() {
//        this.ordersById = new HashMap<>();
//    }
//
//    public void save(OrderHeader order) {
//        ordersById.put(order.getOrderHeaderId(), order);
//        repositoryLogger.info("New order has been saved with order number: " + order.getOrderHeaderId()+".");
//    }
//
//    public OrderHeader findById(String orderId){
//        return ordersById.get(orderId);
//    }
//
//    public Collection<OrderHeader> getAllOrders(){
//        return ordersById.values();
//    }


}
