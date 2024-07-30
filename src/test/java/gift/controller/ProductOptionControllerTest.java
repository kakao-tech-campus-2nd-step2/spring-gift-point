package gift.controller;

import gift.dto.*;
import gift.entity.*;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import gift.service.ProductOptionService;
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
public class ProductOptionControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductOptionService productOptionService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterEach
    public void tearDown() {
        productOptionRepository.deleteAll();
        productRepository.deleteAll();
        optionRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    private Long createCategory() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리");
        return categoryRepository.save(new Category(categoryRequestDto.getName(), categoryRequestDto.getColor(), categoryRequestDto.getImageUrl(), categoryRequestDto.getDescription())).getId();
    }

    private Long createProduct(Long categoryId) {
        ProductRequestDto productRequestDto = new ProductRequestDto("상품1", 1000, "https://example.com/product1.jpg", categoryId);
        return productRepository.save(new Product(new ProductName(productRequestDto.getName()), productRequestDto.getPrice(), productRequestDto.getImageUrl(), categoryRepository.findById(categoryId).get())).getId();
    }

    private Long createOption(String optionName) {
        OptionRequestDto optionRequestDto = new OptionRequestDto(optionName);
        return optionRepository.save(new Option(new OptionName(optionRequestDto.getName()))).getId();
    }

    @Test
    public void 상품_옵션_추가_성공() {
        Long categoryId = createCategory();
        Long productId = createProduct(categoryId);
        Long optionId = createOption("옵션1");

        ProductOptionRequestDto requestDto = new ProductOptionRequestDto(productId, optionId, 10);

        given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post("/api/products/" + productId + "/options")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("productName", equalTo("상품1"))
                .body("optionName", equalTo("옵션1"))
                .body("quantity", equalTo(10));
    }

    @Test
    public void 상품_옵션_조회_성공() {
        Long categoryId = createCategory();
        Long productId = createProduct(categoryId);
        Long optionId1 = createOption("옵션1");
        Long optionId2 = createOption("옵션2");

        productOptionService.addProductOption(new ProductOptionRequestDto(productId, optionId1, 10));
        productOptionService.addProductOption(new ProductOptionRequestDto(productId, optionId2, 20));

        given()
                .when()
                .get("/api/products/" + productId + "/options")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].optionName", equalTo("옵션1"))
                .body("[0].quantity", equalTo(10))
                .body("[1].optionName", equalTo("옵션2"))
                .body("[1].quantity", equalTo(20));
    }

    @Test
    public void 상품_옵션_수정_성공() {
        Long categoryId = createCategory();
        Long productId = createProduct(categoryId);
        Long optionId = createOption("옵션1");

        ProductOptionResponseDto createdProductOption = productOptionService.addProductOption(new ProductOptionRequestDto(productId, optionId, 10));

        ProductOptionRequestDto updateDto = new ProductOptionRequestDto(productId, optionId, 20);

        given()
                .contentType(ContentType.JSON)
                .body(updateDto)
                .when()
                .put("/api/products/options/" + createdProductOption.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("quantity", equalTo(20));
    }

    @Test
    public void 상품_옵션_삭제_성공() {
        Long categoryId = createCategory();
        Long productId = createProduct(categoryId);
        Long optionId = createOption("옵션1");

        ProductOptionResponseDto createdProductOption = productOptionService.addProductOption(new ProductOptionRequestDto(productId, optionId, 10));

        given()
                .when()
                .delete("/api/products/options/" + createdProductOption.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
