package gift.controller;

import gift.dto.CategoryRequestDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.service.CategoryService;
import gift.service.ProductService;
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
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

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
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void 상품_추가_성공() {
        Long categoryId = categoryService.addCategory(new CategoryRequestDto("테스트", "#FF0000", "https://example.com/test.png", "테스트 카테고리")).getId();
        ProductRequestDto productRequestDto = new ProductRequestDto("오둥이 입니다만", 29800, "https://example.com/product2.jpg", categoryId);

        given()
                .contentType(ContentType.JSON)
                .body(productRequestDto)
                .when()
                .post("/api/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo("오둥이 입니다만"))
                .body("price", equalTo(29800))
                .body("imageUrl", equalTo("https://example.com/product2.jpg"))
                .body("category.id", equalTo(categoryId.intValue()));
    }

    @Test
    public void 상품_수정_성공() {
        Long categoryId = categoryService.addCategory(new CategoryRequestDto("테스트", "#FF0000", "https://example.com/test.png", "테스트 카테고리")).getId();
        ProductResponseDto productResponseDto = productService.addProduct(new ProductRequestDto("오둥이 입니다만", 29800, "https://example.com/product2.jpg", categoryId));
        Long productId = productResponseDto.getId();

        ProductRequestDto updateDTO = new ProductRequestDto("오둥이 아닙니다만", 35000, "https://example.com/product3.jpg", categoryId);

        given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/api/products/{id}", productId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("오둥이 아닙니다만"))
                .body("price", equalTo(35000))
                .body("imageUrl", equalTo("https://example.com/product3.jpg"))
                .body("category.id", equalTo(categoryId.intValue()));
    }

    @Test
    public void 모든_상품_조회_성공() {
        Long categoryId = categoryService.addCategory(new CategoryRequestDto("테스트", "#FF0000", "https://example.com/test.png", "테스트 카테고리")).getId();
        ProductResponseDto productResponseDto = productService.addProduct(new ProductRequestDto("오둥이 입니다만", 29800, "https://example.com/product2.jpg", categoryId));

        given()
                .when()
                .get("/api/products?page=0&size=10")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("products[0].id", equalTo(productResponseDto.getId().intValue()))
                .body("products[0].name", equalTo("오둥이 입니다만"))
                .body("products[0].price", equalTo(29800))
                .body("products[0].imageUrl", equalTo("https://example.com/product2.jpg"))
                .body("products[0].category.id", equalTo(categoryId.intValue()))
                .body("currentPage", equalTo(0))
                .body("totalPages", equalTo(1))
                .body("totalItems", equalTo(1));
    }

    @Test
    public void 상품_삭제_성공() {
        Long categoryId = categoryService.addCategory(new CategoryRequestDto("테스트", "#FF0000", "https://example.com/test.png", "테스트 카테고리")).getId();
        ProductResponseDto productResponseDto = productService.addProduct(new ProductRequestDto("오둥이 입니다만", 29800, "https://example.com/product2.jpg", categoryId));
        Long productId = productResponseDto.getId();

        given()
                .when()
                .delete("/api/products/{id}", productId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
