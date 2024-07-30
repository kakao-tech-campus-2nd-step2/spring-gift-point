package gift.controller;

import gift.dto.UserRegisterDto;
import gift.dto.UserResponseDto;
import gift.repository.UserRepository;
import gift.service.UserService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void 회원_가입_성공() {
        UserRegisterDto userRegisterDto = new UserRegisterDto("user@example.com", "password");

        given()
                .contentType(ContentType.JSON)
                .body(userRegisterDto)
                .when()
                .post("/api/users/register")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("email", equalTo("user@example.com"));
    }

    @Test
    public void 모든_사용자_조회_성공() {
        UserRegisterDto userRegisterDto = new UserRegisterDto("user@example.com", "password");
        userService.registerUser(userRegisterDto);

        given()
                .when()
                .get("/api/users")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].email", equalTo("user@example.com"));
    }

    @Test
    public void 사용자_삭제_성공() {
        UserRegisterDto userRegisterDto = new UserRegisterDto("user@example.com", "password");
        UserResponseDto userResponseDto = userService.registerUser(userRegisterDto);
        Long userId = userResponseDto.getId();

        given()
                .when()
                .delete("/api/users/{id}", userId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
