package com.switchfully.eurder.item.api;

import com.switchfully.eurder.customer.api.dto.CreateCustomerDto;
import com.switchfully.eurder.customer.api.dto.CustomerDto;
import com.switchfully.eurder.customer.domain.Customer;
import com.switchfully.eurder.item.api.dto.CreateItemDto;
import com.switchfully.eurder.item.api.dto.ItemDto;
import com.switchfully.eurder.item.api.dto.UpdateItemDto;
import com.switchfully.eurder.item.domain.Item;
import com.switchfully.eurder.item.domain.ItemRepository;
import com.switchfully.eurder.item.service.ItemMapper;
import com.switchfully.eurder.item.service.ItemService;
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

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;

    @Nested
    class CreateItemTest {
        @Test
        void givenNewItem_WhenRegisterItem_ThenReturnNewItem() {
            //  GIVEN
            CreateItemDto newItem = new CreateItemDto("iPhone 12", "Not the newest iPhone", 700, 2);
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            userRepository.addNewAdmin(slash);
            //  WHEN
            ItemDto actualItemDto = RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
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
            Assertions.assertThat(actualItemDto.getItemId()).isNotNull();
            Assertions.assertThat(actualItemDto.getName()).isEqualTo(expectedDto.getName());
            Assertions.assertThat(actualItemDto.getDescription()).isEqualTo(expectedDto.getDescription());
            Assertions.assertThat(actualItemDto.getPrice()).isEqualTo(expectedDto.getPrice());
            Assertions.assertThat(actualItemDto.getAmount()).isEqualTo(expectedDto.getAmount());

        }

        @Test
        void givenNewItem_WhenRegisterItemAnUnauthorizedUser_ThenReturnErrorMessage() {
            //  GIVEN
            CreateItemDto newItem = new CreateItemDto("iPhone 12", "Not the newest iPhone", 700, 2);
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            userRepository.addNewCustomer(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
                    .port(port)
                    .body(newItem)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .post("/items")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .extract()
                    .response();
        }

        @Test
        void givenItemEmptyName_WhenAddedNewItem_ThenReturnErrorMessage() {
            //  GIVEN
            CreateItemDto newItem = new CreateItemDto("", "Not the newest iPhone", 700, 2);
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            userRepository.addNewAdmin(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
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
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            userRepository.addNewAdmin(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
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
            CreateItemDto newItem = new CreateItemDto("iPhone12", "Not the newest iPhone", -7, 2);
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            userRepository.addNewAdmin(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
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
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            userRepository.addNewAdmin(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
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

    @Nested
    class UpdateItemTest {
        @Test
        void givenUpdateItemDto_WhenItemUpdated_ThenReturnUpdatedItem() {
            //  GIVEN
            CreateItemDto newItem = new CreateItemDto("iPhone12", "Not the newest iPhone", 700, 7);
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            UpdateItemDto updateItemDto = new UpdateItemDto("iPhone13", "The newest iPhone", 800, 7);
            Item item = itemMapper.toItem(newItem);
            itemRepository.save(item);
            userRepository.addNewAdmin(slash);
            //  WHEN
            ItemDto actualItemDto = RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
                    .port(port)
                    .body(updateItemDto)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .put("/items/" + item.getItemId())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ItemDto.class);

            Assertions.assertThat(actualItemDto.getName()).isEqualTo(updateItemDto.getName());
            Assertions.assertThat(actualItemDto.getDescription()).isEqualTo(updateItemDto.getDescription());
            Assertions.assertThat(actualItemDto.getPrice()).isEqualTo(updateItemDto.getPrice());
            Assertions.assertThat(actualItemDto.getAmount()).isEqualTo(updateItemDto.getAmount());
            System.out.println(actualItemDto);
        }

        @Test
        void givenUpdateItemDtoWrongItemId_WhenItemUpdated_ThenShowError() {
            //  GIVEN
            CreateItemDto newItem = new CreateItemDto("iPhone12", "Not the newest iPhone", 700, 7);
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            UpdateItemDto updateItemDto = new UpdateItemDto("iPhone13", "The newest iPhone", 800, 7);
            Item item = itemMapper.toItem(newItem);
            itemRepository.save(item);
            userRepository.addNewAdmin(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
                    .port(port)
                    .body(updateItemDto)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .put("/items/3")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract()
                    .response();
        }
        @Test
        void givenUpdateItemEmptyName_WhenItemUpdated_ThenShowError() {
            //  GIVEN
            CreateItemDto newItem = new CreateItemDto("iPhone12", "Not the newest iPhone", 700, 7);
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            UpdateItemDto updateItemDto = new UpdateItemDto("", "The newest iPhone", 800, 7);
            Item item = itemMapper.toItem(newItem);
            itemRepository.save(item);
            userRepository.addNewAdmin(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
                    .port(port)
                    .body(updateItemDto)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .put("/items/"+item.getItemId())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract()
                    .response();
        }
        @Test
        void givenUpdateItemEmptyDescription_WhenItemUpdated_ThenShowError() {
            //  GIVEN
            CreateItemDto newItem = new CreateItemDto("iPhone12", "Not the newest iPhone", 700, 7);
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            UpdateItemDto updateItemDto = new UpdateItemDto("iPhone13", "", 800, 7);
            Item item = itemMapper.toItem(newItem);
            itemRepository.save(item);
            userRepository.addNewAdmin(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
                    .port(port)
                    .body(updateItemDto)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .put("/items/"+item.getItemId())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract()
                    .response();
        }
        @Test
        void givenUpdateInvalidPrice_WhenItemUpdated_ThenShowError() {
            //  GIVEN
            CreateItemDto newItem = new CreateItemDto("iPhone12", "Not the newest iPhone", 700, 7);
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            UpdateItemDto updateItemDto = new UpdateItemDto("iPhone13", "The newest iPhone", -8, 7);
            Item item = itemMapper.toItem(newItem);
            itemRepository.save(item);
            userRepository.addNewAdmin(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
                    .port(port)
                    .body(updateItemDto)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .put("/items/"+item.getItemId())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract()
                    .response();
        }
        @Test
        void givenUpdateInvalidAmount_WhenItemUpdated_ThenShowError() {
            //  GIVEN
            CreateItemDto newItem = new CreateItemDto("iPhone12", "Not the newest iPhone", 700, 7);
            Customer slash = new Customer("Slash", "No Idea", "slash@gnr.com", "089386666", "Garden of Eden");
            UpdateItemDto updateItemDto = new UpdateItemDto("iPhone13", "The newest iPhone", 800, -7);
            Item item = itemMapper.toItem(newItem);
            itemRepository.save(item);
            userRepository.addNewAdmin(slash);
            //  WHEN
            RestAssured
                    .given()
                    .auth()
                    .preemptive()
                    .basic("Slash", "pwd")
                    .port(port)
                    .body(updateItemDto)
                    .contentType(ContentType.JSON)
                    .when()
                    .accept(ContentType.JSON)
                    .put("/items/"+item.getItemId())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract()
                    .response();
        }

    }


}