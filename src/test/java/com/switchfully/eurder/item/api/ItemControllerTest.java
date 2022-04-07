package com.switchfully.eurder.item.api;

import com.switchfully.eurder.customer.api.dto.CreateCustomerDto;
import com.switchfully.eurder.customer.api.dto.CustomerDto;
import com.switchfully.eurder.customer.domain.Customer;
import com.switchfully.eurder.item.api.dto.CreateItemDto;
import com.switchfully.eurder.item.api.dto.ItemDto;
import com.switchfully.eurder.item.domain.Item;
import com.switchfully.eurder.item.service.ItemMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ItemMapper itemMapper;

    @Test
    void givenNewItem_WhenRegisterItem_ThenReturnNewItem() {
        //  GIVEN
        CreateItemDto newItem = new CreateItemDto("iPhone 12", "Not the newest iPhone", 700, 2);

        //  WHEN
        ItemDto actualItemDto = RestAssured
                .given()
                .port(port)
                .body(newItem)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/items")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ItemDto.class);

        Item item = itemMapper.toItem(newItem);
        //  THEN
        ItemDto expectedDto = itemMapper.toDto(item);
        Assertions.assertThat(actualItemDto.getItemId()).isNotEmpty();
        Assertions.assertThat(actualItemDto.getName()).isEqualTo(expectedDto.getName());
        Assertions.assertThat(actualItemDto.getDescription()).isEqualTo(expectedDto.getDescription());
        Assertions.assertThat(actualItemDto.getPrice()).isEqualTo(expectedDto.getPrice());
        Assertions.assertThat(actualItemDto.getAmount()).isEqualTo(expectedDto.getAmount());

    }

    @Test
    void givenItemEmptyName_WhenAddedNewItem_ThenReturnErrorMessage() {
        //  GIVEN
        CreateItemDto newItem = new CreateItemDto("", "Not the newest iPhone", 700, 2);
        //  WHEN
        RestAssured
                .given()
                .port(port)
                .body(newItem)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/items")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response();
    }
    @Test
    void givenItemEmptyDescription_WhenAddedNewItem_ThenReturnErrorMessage() {
        //  GIVEN
        CreateItemDto newItem = new CreateItemDto("iPhone12", "", 700, 2);
        //  WHEN
        RestAssured
                .given()
                .port(port)
                .body(newItem)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/items")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response();
    }
    @Test
    void givenItemInvalidPrice_WhenAddedNewItem_ThenReturnErrorMessage() {
        //  GIVEN
        CreateItemDto newItem = new CreateItemDto("iPhone12", "Not the newest iPhone",-7, 2);
        //  WHEN
        RestAssured
                .given()
                .port(port)
                .body(newItem)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/items")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response();
    }
    @Test
    void givenItemInvalidAmount_WhenAddedNewItem_ThenReturnErrorMessage() {
        //  GIVEN
        CreateItemDto newItem = new CreateItemDto("iPhone12", "Not the newest iPhone", 700, -7);
        //  WHEN
        RestAssured
                .given()
                .port(port)
                .body(newItem)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/items")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response();
    }



}