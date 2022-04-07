package com.switchfully.eurder.order.api;

import com.switchfully.eurder.customer.domain.Customer;
import com.switchfully.eurder.customer.domain.CustomerRepository;
import com.switchfully.eurder.item.domain.Item;
import com.switchfully.eurder.item.domain.ItemRepository;
import com.switchfully.eurder.order.domain.GroupItem;
import com.switchfully.eurder.order.domain.Order;
import com.switchfully.eurder.order.domain.OrderRepository;
import com.switchfully.eurder.order.service.OrderService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderService orderService;

    @Nested
    class CreateOrderTest {
        @Test
        void givenOrder_WhenRegisterOrder_ThenReturnNewOrder() {
            //  GIVEN
            List<Item> fruitItems = List.of(
                    new Item("banana", "tasty banana", 2, 1),
                    new Item("orange", "juicy orange", 1, 100)
            );
            fruitItems.forEach(fruitItem -> itemRepository.save(fruitItem));

            Customer newCustomer = new Customer("Axel", "Rose", "axel_rose@gnr.com", "089386666", "Paradise City 5");
            customerRepository.save(newCustomer);

            List<GroupItem> groupItems = List.of(
                    new GroupItem(fruitItems.get(0).getItemId(), 10),
                    new GroupItem(fruitItems.get(1).getItemId(), 20)
            );

            Order order = new Order(newCustomer.getCustomerId(), groupItems);
            orderRepository.save(order);

            //  WHEN
            Order actualOrder = RestAssured
                    .given()
                    .port(port)
                    .body(order)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .post("/orders")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(Order.class);

            //  THEN
            Order expectedOrder = orderRepository.findById(order.getOrderId());
            Assertions.assertThat(expectedOrder.getCustomer()).isEqualTo(actualOrder.getCustomer());
            Assertions.assertThat(expectedOrder.getTotalPrice()).isEqualTo(actualOrder.getTotalPrice());
            Assertions.assertThat(expectedOrder.getItemGroup()).hasSameElementsAs(actualOrder.getItemGroup());
            System.out.println(expectedOrder);
        }

        @Test
        void givenOrderInStock_WhenRegisterOrder_ThenReturnShippingDatePlus1() {
            //  GIVEN
            List<Item> fruitItems = List.of(
                    new Item("banana", "tasty banana", 2, 1),
                    new Item("orange", "juicy orange", 1, 100)
            );
            fruitItems.forEach(fruitItem -> itemRepository.save(fruitItem));

            Customer newCustomer = new Customer("Axel", "Rose", "axel_rose@gnr.com", "089386666", "Paradise City 5");
            customerRepository.save(newCustomer);

            List<GroupItem> groupItems = List.of(
                    new GroupItem(fruitItems.get(1).getItemId(), 20)
            );

            //  WHEN
            Order order = new Order(newCustomer.getCustomerId(), groupItems);
            orderService.saveNewOrder(order);
            //  THEN
            Order expectedOrder = orderRepository.findById(order.getOrderId());

            Assertions.assertThat(expectedOrder.getItemGroup().get(0).getShippingDate()).isEqualTo(LocalDate.now().plusDays(1));
        }

        @Test
        void givenOrderNotInStock_WhenRegisterOrder_ThenReturnShippingDatePlus8() {
            //  GIVEN
            List<Item> fruitItems = List.of(
                    new Item("banana", "tasty banana", 2, 1),
                    new Item("orange", "juicy orange", 1, 100)
            );
            fruitItems.forEach(fruitItem -> itemRepository.save(fruitItem));

            Customer newCustomer = new Customer("Axel", "Rose", "axel_rose@gnr.com", "089386666", "Paradise City 5");
            customerRepository.save(newCustomer);

            List<GroupItem> groupItems = List.of(
                    new GroupItem(fruitItems.get(0).getItemId(), 20)
            );


            //  WHEN
            Order order = new Order(newCustomer.getCustomerId(), groupItems);
            orderService.saveNewOrder(order);
            //  THEN
            Order expectedOrder = orderRepository.findById(order.getOrderId());

            Assertions.assertThat(expectedOrder.getItemGroup().get(0).getShippingDate()).isEqualTo(LocalDate.now().plusDays(7));
        }

        @Test
        void givenOrder_WhenRegisterOrder_ThenReturnTotalPrice() {
            //  GIVEN
            List<Item> fruitItems = List.of(
                    new Item("banana", "tasty banana", 2, 1),
                    new Item("orange", "juicy orange", 1, 100)
            );
            fruitItems.forEach(fruitItem -> itemRepository.save(fruitItem));

            Customer newCustomer = new Customer("Axel", "Rose", "axel_rose@gnr.com", "089386666", "Paradise City 5");
            customerRepository.save(newCustomer);

            List<GroupItem> groupItems = List.of(
                    new GroupItem(fruitItems.get(0).getItemId(), 20),
                    new GroupItem(fruitItems.get(1).getItemId(), 10)

            );


            //  WHEN
            Order order = new Order(newCustomer.getCustomerId(), groupItems);
            orderService.saveNewOrder(order);
            //  THEN
            Order expectedOrder = orderRepository.findById(order.getOrderId());

            Assertions.assertThat(expectedOrder.getTotalPrice()).isEqualTo(50);
        }
        @Test
        void givenOrderEmptyCustomer_WhenRegisterOrder_ThenReturnTotalPrice() {
            //  GIVEN
            List<Item> fruitItems = List.of(
                    new Item("banana", "tasty banana", 2, 1),
                    new Item("orange", "juicy orange", 1, 100)
            );
            fruitItems.forEach(fruitItem -> itemRepository.save(fruitItem));

            Customer newCustomer = new Customer("Axel", "Rose", "axel_rose@gnr.com", "089386666", "Paradise City 5");
            customerRepository.save(newCustomer);

            List<GroupItem> groupItems = List.of(
                    new GroupItem(fruitItems.get(0).getItemId(), 20),
                    new GroupItem(fruitItems.get(1).getItemId(), 10)

            );


            //  WHEN
            Order order = new Order("", groupItems);
            //  THEN

            RestAssured
                    .given()
                    .port(port)
                    .body(order)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .post("/orders")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract()
                    .response();
        }

        @Test
        void givenOrderNonExistingCustomer_WhenRegisterOrder_ThenReturnTotalPrice() {
            //  GIVEN
            List<Item> fruitItems = List.of(
                    new Item("banana", "tasty banana", 2, 1),
                    new Item("orange", "juicy orange", 1, 100)
            );
            fruitItems.forEach(fruitItem -> itemRepository.save(fruitItem));

            Customer newCustomer = new Customer("Axel", "Rose", "axel_rose@gnr.com", "089386666", "Paradise City 5");
            customerRepository.save(newCustomer);

            List<GroupItem> groupItems = List.of(
                    new GroupItem(fruitItems.get(0).getItemId(), 20),
                    new GroupItem(fruitItems.get(1).getItemId(), 10)

            );


            //  WHEN
            Order order = new Order("1", groupItems);
            //  THEN

            RestAssured
                    .given()
                    .port(port)
                    .body(order)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .post("/orders")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract()
                    .response();
        }

    }


}