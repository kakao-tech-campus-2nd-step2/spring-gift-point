package gift.controller;

import gift.dto.CategoryRequestDto;
import gift.dto.CategoryResponseDto;
import gift.repository.CategoryRepository;
import gift.service.CategoryService;
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
public class CategoryControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterEach
    public void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    public void 카테고리_추가_성공() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리");

        given()
                .contentType(ContentType.JSON)
                .body(categoryRequestDto)
                .when()
                .post("/api/categories")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo("테스트카테고리"))
                .body("color", equalTo("#FF0000"))
                .body("imageUrl", equalTo("https://example.com/test.png"))
                .body("description", equalTo("테스트 카테고리"));
    }

    @Test
    public void 카테고리_수정_성공() {
        CategoryResponseDto categoryResponseDto = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리"));
        Long categoryId = categoryResponseDto.getId();

        CategoryRequestDto updateDTO = new CategoryRequestDto("수정된카테고리", "#00FF00", "https://example.com/test2.png", "수정된 카테고리");

        given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/api/categories/{id}", categoryId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("수정된카테고리"))
                .body("color", equalTo("#00FF00"))
                .body("imageUrl", equalTo("https://example.com/test2.png"))
                .body("description", equalTo("수정된 카테고리"));
    }

    @Test
    public void 모든_카테고리_조회_성공() {
        categoryService.addCategory(new CategoryRequestDto("테스트카테고리1", "#FF0000", "https://example.com/test1.png", "테스트 카테고리1"));
        categoryService.addCategory(new CategoryRequestDto("테스트카테고리2", "#00FF00", "https://example.com/test2.png", "테스트 카테고리2"));

        given()
                .when()
                .get("/api/categories")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].name", equalTo("테스트카테고리1"))
                .body("[1].name", equalTo("테스트카테고리2"));
    }

    @Test
    public void 카테고리_조회_성공() {
        CategoryResponseDto categoryResponseDto = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리"));
        Long categoryId = categoryResponseDto.getId();

        given()
                .when()
                .get("/api/categories/{id}", categoryId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("테스트카테고리"))
                .body("color", equalTo("#FF0000"))
                .body("imageUrl", equalTo("https://example.com/test.png"))
                .body("description", equalTo("테스트 카테고리"));
    }

    @Test
    public void 카테고리_삭제_성공() {
        CategoryResponseDto categoryResponseDto = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리"));
        Long categoryId = categoryResponseDto.getId();

        given()
                .when()
                .delete("/api/categories/{id}", categoryId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
