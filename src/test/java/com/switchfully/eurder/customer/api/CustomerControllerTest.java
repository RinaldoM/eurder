package com.switchfully.eurder.customer.api;

import com.switchfully.eurder.customer.api.dto.CreateCustomerDto;
import com.switchfully.eurder.customer.api.dto.CustomerDto;
import com.switchfully.eurder.customer.domain.Customer;
import com.switchfully.eurder.customer.service.CustomerMapper;
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
class CustomerControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private CustomerMapper customerMapper;


    @Test
    void givenCustomer_WhenRegisterCustomer_ThenReturnNewCustomer() {
        //  GIVEN
        CreateCustomerDto newCustomer = new CreateCustomerDto("Axel", "Rose","axel_rose@gnr.com","089386666","Paradise City 5");
        //  WHEN
        CustomerDto actualCustomerDto = RestAssured
                .given()
                .port(port)
                .body(newCustomer)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/customers")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(CustomerDto.class);
        Customer customer  = customerMapper.toCustomer(newCustomer);
        //  THEN

        CustomerDto expectedCustomerDto = customerMapper.toCustomerDto(customer);
        Assertions.assertThat(actualCustomerDto.getCustomerId()).isNotBlank();
        Assertions.assertThat(actualCustomerDto.getFirstName()).isEqualTo(expectedCustomerDto.getFirstName());
        Assertions.assertThat(actualCustomerDto.getLastName()).isEqualTo(expectedCustomerDto.getLastName());
        Assertions.assertThat(actualCustomerDto.getEmail()).isEqualTo(expectedCustomerDto.getEmail());
        Assertions.assertThat(actualCustomerDto.getAddress()).isEqualTo(expectedCustomerDto.getAddress());
    }

    @Test
    void givenCustomerEmptyFirstName_WhenRegisterCustomer_ThenReturnErrorMessage() {
        //  GIVEN
        CreateCustomerDto newCustomer = new CreateCustomerDto("", "Rose","axel_rose@gnr.com","089386666","Paradise City 5");
        //  WHEN
        RestAssured
                .given()
                .port(port)
                .body(newCustomer)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/customers")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response();
    }
    @Test
    void givenCustomerEmptyLastName_WhenRegisterCustomer_ThenReturnErrorMessage() {
        //  GIVEN
        CreateCustomerDto newCustomer = new CreateCustomerDto("Axel", "","axel_rose@gnr.com","089386666","Paradise City 5");
        //  WHEN
        RestAssured
                .given()
                .port(port)
                .body(newCustomer)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/customers")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response();
    }
    @Test
    void givenCustomerEmptyEmail_WhenRegisterCustomer_ThenReturnErrorMessage() {
        //  GIVEN
        CreateCustomerDto newCustomer = new CreateCustomerDto("Axel", "Rose","","089386666","Paradise City 5");
        //  WHEN
        RestAssured
                .given()
                .port(port)
                .body(newCustomer)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/customers")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response();
    }
    @Test
    void givenCustomerInvalidEmail_WhenRegisterCustomer_ThenReturnErrorMessage() {
        //  GIVEN
        CreateCustomerDto newCustomer = new CreateCustomerDto("Axel", "Rose","axel.rose.gnr.com","089386666","Paradise City 5");
        //  WHEN
        RestAssured
                .given()
                .port(port)
                .body(newCustomer)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/customers")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response();
    }
    @Test
    void givenCustomerEmptyPhoneNumber_WhenRegisterCustomer_ThenReturnErrorMessage() {
        //  GIVEN
        CreateCustomerDto newCustomer = new CreateCustomerDto("Axel", "Rose","axel.rose@gnr.com","","Paradise City 5");
        //  WHEN
        RestAssured
                .given()
                .port(port)
                .body(newCustomer)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/customers")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response();
    }
    @Test
    void givenCustomerEmptyAddress_WhenRegisterCustomer_ThenReturnErrorMessage() {
        //  GIVEN
        CreateCustomerDto newCustomer = new CreateCustomerDto("Axel", "Rose","axel.rose@gnr.com","089386666","");
        //  WHEN
        RestAssured
                .given()
                .port(port)
                .body(newCustomer)
                .contentType(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .post("/customers")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response();
    }




}