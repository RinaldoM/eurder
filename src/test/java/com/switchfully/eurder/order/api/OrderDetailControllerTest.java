package com.switchfully.eurder.order.api;

import com.switchfully.eurder.customer.domain.Customer;
import com.switchfully.eurder.customer.domain.CustomerRepository;
import com.switchfully.eurder.customer.service.CustomerService;
import com.switchfully.eurder.item.domain.Item;
import com.switchfully.eurder.item.domain.ItemRepository;
import com.switchfully.eurder.item.service.ItemService;
import com.switchfully.eurder.order.api.dto.CreateOrderDetailDto;
import com.switchfully.eurder.order.api.dto.CreateOrderHeaderDto;
import com.switchfully.eurder.order.api.dto.OrderDetailDto;
import com.switchfully.eurder.order.domain.OrderHeader;
import com.switchfully.eurder.order.domain.OrderHeaderRepository;
import com.switchfully.eurder.order.service.OrderDetailService;
import com.switchfully.eurder.order.service.OrderHeaderMapper;
import com.switchfully.eurder.order.service.OrderHeaderService;
import com.switchfully.eurder.security.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderDetailControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private ItemService itemService;
    @Autowired
    private OrderHeaderService orderHeaderService;
    @Autowired
    private OrderDetailService orderDetailService;


    @Test
    void givenOrderDetail_WhenRegisterOrderDetail_ThenReturnNewOrderDetail() {
        //  GIVEN

        CreateOrderDetailDto createOrderDetailDto = new CreateOrderDetailDto(1L, 10,1L);

        //  WHEN
        OrderDetailDto actualDto = RestAssured
                .given()
                .auth()
                .preemptive()
                .basic("Admin", "pwd")
                .port(port)
                .body(createOrderDetailDto)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/order-details")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(OrderDetailDto.class);

        //  THEN
        Assertions.assertThat(actualDto.getItemName()).isEqualTo("BANANA");
    }

    @Test
    void givenOrderDetailWithNonExistingItemId_WhenRegisterOrderDetail_ThenReturnBadRequest() {
        CreateOrderDetailDto createOrderDetailDto = new CreateOrderDetailDto(5L, 10,1L);

        RestAssured
                .given()
                .auth()
                .preemptive()
                .basic("Admin", "pwd")
                .port(port)
                .body(createOrderDetailDto)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/order-details")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .response();
    }
    @Test
    void givenOrderDetailWithNonExistingOrderHeaderId_WhenRegisterOrderDetail_ThenReturnBadRequest() {
        CreateOrderDetailDto createOrderDetailDto = new CreateOrderDetailDto(1L, 10,5L);

        RestAssured
                .given()
                .auth()
                .preemptive()
                .basic("Admin", "pwd")
                .port(port)
                .body(createOrderDetailDto)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/order-details")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .response();
    }
    @Test
    void givenOrderDetailWithNegativeAmount_WhenRegisterOrderDetail_ThenReturnBadRequest() {
        CreateOrderDetailDto createOrderDetailDto = new CreateOrderDetailDto(1L, -10,1L);

        RestAssured
                .given()
                .auth()
                .preemptive()
                .basic("Admin", "pwd")
                .port(port)
                .body(createOrderDetailDto)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/order-details")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .response();
    }
    @Test
    void givenOrderDetail_WhenRegisterOrderDetail_ThenReturnTotalPriceOfDetailLine() {
        //  GIVEN

        CreateOrderDetailDto createOrderDetailDto = new CreateOrderDetailDto(1L, 10,1L);

        //  WHEN
        OrderDetailDto actualDto = RestAssured
                .given()
                .auth()
                .preemptive()
                .basic("Admin", "pwd")
                .port(port)
                .body(createOrderDetailDto)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/order-details")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(OrderDetailDto.class);

        //  THEN
        Assertions.assertThat(actualDto.getGroupItemPrice()).isEqualTo(10);
    }

    @Test
    void givenOrderDetail_WhenRegisterOrderDetail_ThenCheckAmountOfStock() {
        //  GIVEN

        CreateOrderDetailDto createOrderDetailDto = new CreateOrderDetailDto(1L, 10,1L);

        //  WHEN
        OrderDetailDto actualDto = RestAssured
                .given()
                .auth()
                .preemptive()
                .basic("Admin", "pwd")
                .port(port)
                .body(createOrderDetailDto)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/order-details")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(OrderDetailDto.class);

        //  THEN
        Assertions.assertThat(itemService.findItemById(createOrderDetailDto.getItemId()).getAmount()).isEqualTo(90);
    }
    @Test
    void givenOrderDetailItemInStock_WhenRegisterOrderDetail_ThenShippingDateTodayPlus1() {
        //  GIVEN

        CreateOrderDetailDto createOrderDetailDto = new CreateOrderDetailDto(1L, 10,1L);

        //  WHEN
        OrderDetailDto actualDto = RestAssured
                .given()
                .auth()
                .preemptive()
                .basic("Admin", "pwd")
                .port(port)
                .body(createOrderDetailDto)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/order-details")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(OrderDetailDto.class);

        //  THEN
        Assertions.assertThat(actualDto.getShippingDate()).isEqualTo(LocalDate.now().plusDays(1));
    }

    @Test
    void givenOrderDetailItemOutOfStock_WhenRegisterOrderDetail_ThenShippingDateTodayPlus7() {
        //  GIVEN
        CreateOrderDetailDto createOrderDetailDto = new CreateOrderDetailDto(1L, 200,1L);
        //  WHEN
        OrderDetailDto actualDto = RestAssured
                .given()
                .auth()
                .preemptive()
                .basic("Admin", "pwd")
                .port(port)
                .body(createOrderDetailDto)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/order-details")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(OrderDetailDto.class);
        //  THEN
        Assertions.assertThat(actualDto.getShippingDate()).isEqualTo(LocalDate.now().plusDays(7));
    }

}