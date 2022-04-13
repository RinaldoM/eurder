package com.switchfully.eurder.order.api;

import com.switchfully.eurder.customer.domain.Customer;
import com.switchfully.eurder.customer.domain.CustomerRepository;
import com.switchfully.eurder.item.domain.Item;
import com.switchfully.eurder.item.domain.ItemRepository;
import com.switchfully.eurder.order.api.dto.CreateGroupItemDto;
import com.switchfully.eurder.order.api.dto.CreateOrderDto;
import com.switchfully.eurder.order.domain.GroupItem;
import com.switchfully.eurder.order.domain.Order;
import com.switchfully.eurder.order.domain.OrderReport;
import com.switchfully.eurder.order.domain.OrderRepository;
import com.switchfully.eurder.order.service.OrderMapper;
import com.switchfully.eurder.order.service.OrderService;
import com.switchfully.eurder.security.UserRepository;
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
import java.util.Collection;
import java.util.List;

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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderMapper orderMapper;

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
            userRepository.addNewCustomer(newCustomer);

            List<CreateGroupItemDto> groupItems = List.of(
                    new CreateGroupItemDto(fruitItems.get(0).getItemId(), 10),
                    new CreateGroupItemDto(fruitItems.get(1).getItemId(), 20)
            );
            CreateOrderDto createOrderDTO = new CreateOrderDto(newCustomer.getCustomerId(),groupItems);

            //  WHEN
            Order actualOrder = RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Axel", "pwd")
                    .port(port)
                    .body(createOrderDTO)
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
            Order expectedOrder = orderService.saveNewOrder(createOrderDTO);
            Assertions.assertThat(expectedOrder.getCustomerId()).isEqualTo(actualOrder.getCustomerId());
            Assertions.assertThat(expectedOrder.getTotalPrice()).isEqualTo(actualOrder.getTotalPrice());
            Assertions.assertThat(expectedOrder.getItemGroup()).hasSameElementsAs(actualOrder.getItemGroup());
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

            List<CreateGroupItemDto> groupItems = List.of(
                    new CreateGroupItemDto(fruitItems.get(1).getItemId(), 20)
            );

            //  WHEN
            CreateOrderDto createOrderDTO = new CreateOrderDto(newCustomer.getCustomerId(), groupItems);

            //  THEN
            Order order =orderService.saveNewOrder(createOrderDTO);


            Assertions.assertThat(order.getItemGroup().get(0).getShippingDate()).isEqualTo(LocalDate.now().plusDays(1));
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

            List<CreateGroupItemDto> groupItems = List.of(
                    new CreateGroupItemDto(fruitItems.get(0).getItemId(), 20)
            );


            //  WHEN
            CreateOrderDto createOrderDTO = new CreateOrderDto(newCustomer.getCustomerId(), groupItems);

            //  THEN
            Order order =orderService.saveNewOrder(createOrderDTO);

            Assertions.assertThat(order.getItemGroup().get(0).getShippingDate()).isEqualTo(LocalDate.now().plusDays(7));
        }

        @Test
        void givenOrder_WhenRegisterOrder_ThenReturnTotalPrice50() {
            //  GIVEN
            List<Item> fruitItems = List.of(
                    new Item("banana", "tasty banana", 2, 1),
                    new Item("orange", "juicy orange", 1, 100)
            );
            fruitItems.forEach(fruitItem -> itemRepository.save(fruitItem));

            Customer newCustomer = new Customer("Axel", "Rose", "axel_rose@gnr.com", "089386666", "Paradise City 5");

            customerRepository.save(newCustomer);

            List<CreateGroupItemDto> groupItems = List.of(
                    new CreateGroupItemDto(fruitItems.get(0).getItemId(), 20),
                    new CreateGroupItemDto(fruitItems.get(1).getItemId(), 10)

            );


            //  WHEN
            CreateOrderDto createOrderDTO = new CreateOrderDto(newCustomer.getCustomerId(), groupItems);

            //  THEN
            Order order =orderService.saveNewOrder(createOrderDTO);

            Assertions.assertThat(order.getTotalPrice()).isEqualTo(50);
        }

        @Test
        void givenOrderEmptyCustomer_WhenRegisterOrder_ThenReturnTotalPrice() {
            //  GIVEN
            List<Item> fruitItems = List.of(
                    new Item("banana", "tasty banana", 2, 1),
                    new Item("orange", "juicy orange", 1, 100)
            );
            fruitItems.forEach(fruitItem -> itemRepository.save(fruitItem));
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            userRepository.addNewAdmin(slash);

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
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
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
        void givenOrderNonExistingCustomer_WhenRegisterOrder_ThenReturnError() {
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
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            userRepository.addNewAdmin(slash);

            //  WHEN
            Order order = new Order("1", groupItems);
            //  THEN

            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
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

    @Nested
    class ReportTest{
        @Test
        void givenCustomerNumber_reportAsked_thenReturnReport() {
            //  GIVEN
            List<Item> fruitItems = List.of(
                    new Item("banana", "tasty banana", 2, 1),
                    new Item("orange", "juicy orange", 1, 100)
            );
            fruitItems.forEach(fruitItem -> itemRepository.save(fruitItem));

            Customer newCustomer = new Customer("Axel", "Rose", "axel_rose@gnr.com", "089386666", "Paradise City 5");
            customerRepository.save(newCustomer);
            userRepository.addNewCustomer(newCustomer);

            List<CreateGroupItemDto> groupItems = List.of(
                    new CreateGroupItemDto(fruitItems.get(0).getItemId(), 10),
                    new CreateGroupItemDto(fruitItems.get(1).getItemId(), 20)
            );
            CreateOrderDto createOrderDTO = new CreateOrderDto(newCustomer.getCustomerId(),groupItems);
            orderRepository.save(orderMapper.toOrder(createOrderDTO));
            Collection<Order> orderList = orderRepository.getAllOrders();

            double totalPrice =orderList.stream().mapToDouble(Order::getTotalPrice).sum();

            OrderReport expectedReport = new OrderReport(orderMapper.toOrderDto(orderList),totalPrice);

            //  WHEN
            OrderReport actualReport = RestAssured
                    .given()
                    .param("customer",newCustomer.getCustomerId())
                    .auth()
                    .preemptive()
                    .basic("Axel", "pwd")
                    .port(port)
                    .body(expectedReport)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .get("/orders")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(OrderReport.class);

            //  THEN
            Assertions.assertThat(expectedReport.getTotalOrderPrice()).isEqualTo(actualReport.getTotalOrderPrice());
            Assertions.assertThat(expectedReport.getOrderList()).hasSameElementsAs(actualReport.getOrderList());
        }
        @Test
        void givenWrongCustomerNumber_reportAsked_thenReturnError() {
            //  GIVEN
            List<Item> fruitItems = List.of(
                    new Item("banana", "tasty banana", 2, 1),
                    new Item("orange", "juicy orange", 1, 100)
            );
            fruitItems.forEach(fruitItem -> itemRepository.save(fruitItem));

            Customer newCustomer = new Customer("Axel", "Rose", "axel_rose@gnr.com", "089386666", "Paradise City 5");
            customerRepository.save(newCustomer);
            userRepository.addNewCustomer(newCustomer);

            List<CreateGroupItemDto> groupItems = List.of(
                    new CreateGroupItemDto(fruitItems.get(0).getItemId(), 10),
                    new CreateGroupItemDto(fruitItems.get(1).getItemId(), 20)
            );
            CreateOrderDto createOrderDTO = new CreateOrderDto(newCustomer.getCustomerId(),groupItems);
            orderRepository.save(orderMapper.toOrder(createOrderDTO));
            Collection<Order> orderList = orderRepository.getAllOrders();

            double totalPrice =orderList.stream().mapToDouble(Order::getTotalPrice).sum();

            OrderReport expectedReport = new OrderReport(orderMapper.toOrderDto(orderList),totalPrice);

            //  WHEN
           RestAssured
                    .given()
                    .param("customer","1")
                    .auth()
                    .preemptive()
                    .basic("Axel", "pwd")
                    .port(port)
                    .body(expectedReport)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .get("/orders")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract()
                    .response();

        }
    }


}

