package com.switchfully.eurder.customer.api;

import com.switchfully.eurder.customer.api.dto.CreateCustomerDto;
import com.switchfully.eurder.customer.api.dto.CustomerDto;
import com.switchfully.eurder.customer.domain.Customer;
import com.switchfully.eurder.customer.domain.CustomerRepository;
import com.switchfully.eurder.customer.service.CustomerMapper;
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

import java.util.Arrays;
import java.util.List;

@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;


    @Nested
    class CreationOfCustomerTest {
        @Test
        void givenCustomer_WhenRegisterCustomer_ThenReturnNewCustomer() {
            //  GIVEN
            CreateCustomerDto newCustomer = new CreateCustomerDto("Axel", "Rose", "axel_rose@gnr.com", "089386666", "Paradise City 5");
            Customer customer = customerMapper.toCustomer(newCustomer);
            userRepository.addNewCustomer(customer);

            //  WHEN
            CustomerDto actualCustomerDto = RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Axel", "pwd")
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
            //  THEN

            CustomerDto expectedCustomerDto = customerMapper.toCustomerDto(customer);
            Assertions.assertThat(actualCustomerDto.getCustomerId()).isNotNull();
            Assertions.assertThat(actualCustomerDto.getFirstName()).isEqualTo(expectedCustomerDto.getFirstName());
            Assertions.assertThat(actualCustomerDto.getLastName()).isEqualTo(expectedCustomerDto.getLastName());
            Assertions.assertThat(actualCustomerDto.getEmail()).isEqualTo(expectedCustomerDto.getEmail());
            Assertions.assertThat(actualCustomerDto.getAddress()).isEqualTo(expectedCustomerDto.getAddress());
        }

        @Test
        void givenCustomerEmptyFirstName_WhenRegisterCustomer_ThenReturnErrorMessage() {
            //  GIVEN
            CreateCustomerDto newCustomer = new CreateCustomerDto("", "Rose", "axel_rose@gnr.com", "089386666", "Paradise City 5");
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            userRepository.addNewCustomer(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
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
            CreateCustomerDto newCustomer = new CreateCustomerDto("Axel", "", "axel_rose@gnr.com", "089386666", "Paradise City 5");
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            userRepository.addNewCustomer(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
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
            CreateCustomerDto newCustomer = new CreateCustomerDto("Axel", "Rose", "", "089386666", "Paradise City 5");
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            userRepository.addNewCustomer(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
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
            CreateCustomerDto newCustomer = new CreateCustomerDto("Axel", "Rose", "axel.rose.gnr.com", "089386666", "Paradise City 5");
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            userRepository.addNewCustomer(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
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
            CreateCustomerDto newCustomer = new CreateCustomerDto("Axel", "Rose", "axel.rose@gnr.com", "", "Paradise City 5");
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            userRepository.addNewCustomer(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
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
            CreateCustomerDto newCustomer = new CreateCustomerDto("Axel", "Rose", "axel.rose@gnr.com", "089386666", "");
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            userRepository.addNewCustomer(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
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

    @Nested
    class ShowCustomerTest {
        @Test
        void whenAllCustomersAsked_showAllCustomers() {


            //  WHEN
            CustomerDto[] actualList = RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Admin", "pwd")
                    .port(port)

                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .get("/customers")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(CustomerDto[].class);
            //  THEN

            Assertions.assertThat(actualList.length).isEqualTo(1);
        }



        @Test
        void whenOneCustomerAsked_showThatCustomer() {
            //  GIVEN
            Customer customer = new Customer("Jimi", "Hendrix", "jimi.hendrix@voodoochild.com", "089386446", "Heaven");
            customerRepository.save(customer);
            userRepository.addNewAdmin(customer);

            //  WHEN
            CustomerDto actualCustomer = RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Jimi", "pwd")
                    .port(port)
                    .body(customer)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .get("/customers/" + customer.getCustomerId())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(CustomerDto.class);
            CustomerDto expectedCustomer = customerMapper.toCustomerDto(customer);
            //  THEN

            Assertions.assertThat(actualCustomer).isEqualTo(expectedCustomer);
        }

        @Test
        void whenOneCustomerAskedThatDoesNotExist_showError() {
            //  GIVEN
            Customer customer = new Customer("Jimi", "Hendrix", "jimi.hendrix@voodoochild.com", "089386446", "Heaven");
            customerRepository.save(customer);
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            userRepository.addNewAdmin(slash);

            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
                    .port(port)
                    .body(customer)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .get("/customers/3")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract()
                    .response();
        }

        @Test
        void givenNonAuthorizedUser_whenOneCustomerAsked_showErrorMessage() {
            //  GIVEN
            Customer customer = new Customer("Jimi", "Hendrix", "jimi.hendrix@voodoochild.com", "089386446", "Heaven");
            customerRepository.save(customer);
            userRepository.addNewCustomer(customer);

            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Jimi", "pwd")
                    .port(port)
                    .body(customer)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .get("/customers/" + customer.getCustomerId())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .extract()
                    .response();

        }
        @Test
        void givenNonAuthorizedUser_whenAllCustomerAsked_showErrorMessage() {
            //  GIVEN
            Customer customer = new Customer("Jimi", "Hendrix", "jimi.hendrix@voodoochild.com", "089386446", "Heaven");
            customerRepository.save(customer);
            userRepository.addNewCustomer(customer);

            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Jimi", "pwd")
                    .port(port)
                    .body(customer)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .get("/customers/")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .extract()
                    .response();

        }

        @Test
        void givenWrongPassWordOfUser_whenAllCustomerAsked_showErrorMessage() {
            //  GIVEN
            Customer customer = new Customer("Jimi", "Hendrix", "jimi.hendrix@voodoochild.com", "089386446", "Heaven");
            customerRepository.save(customer);
            userRepository.addNewAdmin(customer);

            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Jimi", "password")
                    .port(port)
                    .body(customer)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .get("/customers/")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .extract()
                    .response();

        }
        @Test
        void givenWrongUserNameOfUser_whenAllCustomerAsked_showErrorMessage() {
            //  GIVEN
            Customer customer = new Customer("Jimi", "Hendrix", "jimi.hendrix@voodoochild.com", "089386446", "Heaven");
            customerRepository.save(customer);
            userRepository.addNewAdmin(customer);

            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Jimy", "pwd")
                    .port(port)
                    .body(customer)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .get("/customers/")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .extract()
                    .response();

        }


    }


}