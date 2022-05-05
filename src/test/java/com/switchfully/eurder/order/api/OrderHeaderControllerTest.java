package com.switchfully.eurder.order.api;

import com.switchfully.eurder.customer.domain.CustomerRepository;
import com.switchfully.eurder.customer.service.CustomerService;
import com.switchfully.eurder.item.domain.ItemRepository;
import com.switchfully.eurder.order.api.dto.CreateOrderDetailDto;
import com.switchfully.eurder.order.api.dto.CreateOrderHeaderDto;
import com.switchfully.eurder.order.api.dto.OrderHeaderDto;
import com.switchfully.eurder.order.domain.OrderHeader;
import com.switchfully.eurder.order.domain.OrderHeaderRepository;
import com.switchfully.eurder.order.service.OrderDetailService;
import com.switchfully.eurder.order.service.OrderHeaderMapper;
import com.switchfully.eurder.order.service.OrderHeaderService;
import com.switchfully.eurder.security.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderHeaderControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private OrderHeaderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderHeaderService orderHeaderService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderHeaderMapper orderMapper;
    @Autowired
    private OrderDetailService orderDetailService;



    @Nested
    class CreateOrderTest {
        @Test
        void givenOrder_WhenRegisterOrder_ThenReturnNewOrder() {
            //  GIVEN
            CreateOrderHeaderDto createOrderDTO = new CreateOrderHeaderDto(1L);

            //  WHEN
            OrderHeaderDto actualOrder = RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Admin", "pwd")
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
                    .as(OrderHeaderDto.class);

            //  THEN
            Assertions.assertThat(actualOrder.getCustomerId()).isEqualTo("Rinaldo");
        }

        @Test
        void givenOrderWithNonExistingCustomer_WhenRegisterOrder_ThenReturnBadRequest() {
            //  GIVEN
            CreateOrderHeaderDto createOrderDTO = new CreateOrderHeaderDto(5L);

            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Admin", "pwd")
                    .port(port)
                    .body(createOrderDTO)
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
    @Test
    void givenOrderWithNonExistingCustomer_WhenRegisterOrder_ThenReturnBadRequest() {
        //  GIVEN
        CreateOrderHeaderDto createOrderDTO = new CreateOrderHeaderDto(1L);
        OrderHeader orderHeader = orderHeaderService.saveNewOrder(createOrderDTO);
        CreateOrderDetailDto createOrderDetailDto = new CreateOrderDetailDto(1L,1,orderHeader.getOrderHeaderId());
        CreateOrderDetailDto createOrderDetailDto2 = new CreateOrderDetailDto(1L,2,orderHeader.getOrderHeaderId());
        orderDetailService.saveOrderDetail(createOrderDetailDto, orderHeader);
        orderDetailService.saveOrderDetail(createOrderDetailDto2, orderHeader);
        Assertions.assertThat(orderHeader.getTotalPrice()).isEqualTo(3);
    }

}

