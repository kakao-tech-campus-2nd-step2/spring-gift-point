package gift.controller;

import gift.dto.PointChargeRequestDto;
import gift.entity.Point;
import gift.entity.User;
import gift.repository.PointRepository;
import gift.repository.UserRepository;
import gift.service.PointService;
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
public class PointControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointService pointService;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterEach
    public void tearDown() {
        pointRepository.deleteAll();
        userRepository.deleteAll();
    }
    
    @Test
    public void 포인트_충전_성공() {
        User user = userRepository.save(new User("testuser@example.com", "password"));
        pointRepository.save(new Point(user));
        pointService.chargePoints(new PointChargeRequestDto(user.getId(), 1000));
        PointChargeRequestDto requestDto = new PointChargeRequestDto(user.getId(), 500);

        given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post("/api/points/charge")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("userId", equalTo(user.getId().intValue()))
                .body("points", equalTo(1500));
    }

    @Test
    public void 포인트_충전_실패_사용자_없음() {
        PointChargeRequestDto requestDto = new PointChargeRequestDto(999L, 500);

        given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post("/api/points/charge")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
