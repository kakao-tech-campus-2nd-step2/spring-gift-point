package gift.product;

import gift.global.exception.custrom.NotFoundException;
import gift.member.business.dto.JwtToken;
import gift.member.presentation.dto.RequestMemberDto;
import gift.product.business.dto.OptionIn;
import gift.product.business.service.ProductService;
import gift.product.persistence.entity.Category;
import gift.product.persistence.entity.Option;
import gift.product.persistence.entity.Product;
import gift.product.persistence.repository.CategoryJpaRepository;
import gift.product.persistence.repository.ProductJpaRepository;
import gift.product.presentation.dto.OptionRequest;
import gift.product.presentation.dto.ProductRequest;
import gift.product.persistence.repository.ProductRepository;
import gift.product.presentation.dto.ProductResponse;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private static HttpHeaders headers;

    private static Category category;

    private static Product dummyProduct;

    @BeforeAll
    static void setUp(@Autowired TestRestTemplate restTemplate,
        @Autowired CategoryJpaRepository categoryRepository,
        @Autowired ProductJpaRepository productRepository,
        @LocalServerPort int port
    ) {
        //set token
        RequestMemberDto requestMemberDto = new RequestMemberDto(
            "test@example.com",
            "test");

        String url = "http://localhost:" + port + "/api/members/register";
        var response = restTemplate.postForEntity(url, requestMemberDto, JwtToken.class);
        var jwtToken = response.getBody();
        String accessToken = jwtToken.accessToken();

        headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        //set category
        category = categoryRepository.save(new Category("카테고리"));

        //set product, option
        var product = new Product("이름", "설명", 1000, "http://test.com", category);
        product.addOptions(List.of(new Option("옵션", 10)));
        dummyProduct = productRepository.save(product);
    }

    @Test
    @Transactional
    void testCreateProduct() {
        // given
        ProductRequest.Create dto = new ProductRequest.Create(
            "테스트 상품_()[]+-",
            1000,
            "테스트 상품 설명",
            "http://test.com",
            category.getId(),
            List.of(new OptionRequest.Create("옵션", 10))
        );

        String url = "http://localhost:" + port + "/api/products";
        var entity = new HttpEntity<>(dto, headers);

        // when
        var response = restTemplate.exchange(url, HttpMethod.POST, entity, Long.class);

        // then
        assertAll(
            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
            () -> assertThat(response.getBody()).isNotNull()
        );

        Product product = productRepository.getProductById(response.getBody());
        assertAll(
            () -> assertThat(product.getName()).isEqualTo(dto.name()),
            () -> assertThat(product.getPrice()).isEqualTo(dto.price()),
            () -> assertThat(product.getDescription()).isEqualTo(dto.description()),
            () -> assertThat(product.getUrl()).isEqualTo(dto.imageUrl())
        );
        productRepository.deleteProductById(response.getBody());
    }

    @Test
    @Transactional
    void testGetProduct() {
        // given
        String getUrl = "http://localhost:" + port + "/api/products/" + dummyProduct.getId();
        var entity = new HttpEntity<>(headers);

        // when
        var response = restTemplate.exchange(getUrl, HttpMethod.GET, entity,
            ProductResponse.WithOptions.class);

        // then
        assertAll(
            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
            () -> assertThat(response.getBody()).isNotNull()
        );
        ProductResponse.WithOptions responseProductDto = response.getBody();
        System.out.println("_info_" + responseProductDto);
        assertAll(
            () -> assertThat(responseProductDto.id()).isEqualTo(dummyProduct.getId()),
            () -> assertThat(responseProductDto.name()).isEqualTo(dummyProduct.getName()),
            () -> assertThat(responseProductDto.price()).isEqualTo(dummyProduct.getPrice()),
            () -> assertThat(responseProductDto.description()).isEqualTo(
                dummyProduct.getDescription()),
            () -> assertThat(responseProductDto.imageUrl()).isEqualTo(dummyProduct.getUrl()),
            () -> assertThat(responseProductDto.options().size()).isEqualTo(dummyProduct.getOptions().size())
        );
    }

    @Test
    @Transactional
    void testUpdateProduct() {
        // given
        ProductRequest.Update updateProductDto = new ProductRequest.Update(
            "수정",
            2000,
            "수정 설명",
            "http://updated.com",
            category.getId()
        );

        String updateUrl = "http://localhost:" + port + "/api/products/" + dummyProduct.getId();

        var entity = new HttpEntity<>(updateProductDto, headers);

        // when
        var response = restTemplate.exchange(updateUrl, HttpMethod.PUT, entity, Long.class);

        // then
        assertAll(
            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
            () -> assertThat(response.getBody()).isNotNull()
        );
        Long updatedId = response.getBody();
        Product product = productRepository.getProductById(updatedId);
        assertAll(
            () -> assertThat(product.getName()).isEqualTo(updateProductDto.name()),
            () -> assertThat(product.getPrice()).isEqualTo(updateProductDto.price()),
            () -> assertThat(product.getDescription()).isEqualTo(updateProductDto.description()),
            () -> assertThat(product.getUrl()).isEqualTo(updateProductDto.imageUrl())

        );
        productRepository.saveProduct(dummyProduct);
    }

    @Test
    @Transactional
    void testDeleteProduct() {
        // given
        String deleteUrl = "http://localhost:" + port + "/api/products";

        ProductRequest.Ids requestProductIdsDto = new ProductRequest.Ids(List.of(dummyProduct.getId()));
        var deleteEntity = new HttpEntity<>(requestProductIdsDto, headers);

        // when
        var response = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, deleteEntity,
            Void.class);

        // then
        assertThrows(NotFoundException.class,
            () -> productRepository.getProductById(dummyProduct.getId()));

        // rollback
        productRepository.saveProduct(dummyProduct);
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    @Transactional
    void testCreateProduct_Failure(String name, String imageUrl) {
        // given
        String url = "http://localhost:" + port + "/api/products";

        ProductRequest.Create productRequestCreate = new ProductRequest.Create(
            name,
            1000,
            "테스트 상품 설명",
            imageUrl,
            category.getId(),
            List.of(new OptionRequest.Create("옵션", 10))
        );

        var entity = new HttpEntity<>(productRequestCreate, headers);

        // when
        var response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
            Arguments.of(null, "http://test.com"),
            Arguments.of("#@", "http://test.com"),
            Arguments.of("카카오", "http://test.com"),
            Arguments.of("asdf", "url 형식 아님")
        );
    }

    @Test
    @Transactional
    void testGetProduct_NotFound() {
        // given
        Long nonExistentId = 999L;
        String getUrl = "http://localhost:" + port + "/api/products/" + nonExistentId;

        var entity = new HttpEntity<>(headers);

        // when
        var responseEntity = restTemplate.exchange(getUrl, HttpMethod.GET, entity, String.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Transactional
    void testUpdateProduct_NotFound() {
        // given
        Long nonExistentId = 999L;
        var updateProductDto = new ProductRequest.Update("수정된 상품", 2000, "수정된 상품 설명",
            "http://updated.com", category.getId());
        String updateUrl = "http://localhost:" + port + "/api/products/" + nonExistentId;
        var entity = new HttpEntity<>(updateProductDto, headers);

        // when
        ResponseEntity<String> updateResponseEntity = restTemplate.exchange(updateUrl,
            HttpMethod.PUT, entity, String.class);

        // then
        assertThat(updateResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Transactional
    void testDeleteProduct_NotFound() {
        // given
        Long nonExistentId = 999L;
        String deleteUrl = "http://localhost:" + port + "/api/products/" + nonExistentId;

        var entity = new HttpEntity<>(headers);

        // when
        ResponseEntity<String> responseEntity = restTemplate.exchange(deleteUrl, HttpMethod.DELETE,
            entity, String.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Transactional
    void testGetProductByPage_SizeFail() {
        // given
        String getUrl = "http://localhost:" + port + "/api/products?page=0&size=101";

        // when
        var entity = new HttpEntity<>(headers);
        var getResponseEntity = restTemplate.exchange(getUrl, HttpMethod.GET, entity, String.class);

        // then
        assertThat(getResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(getResponseEntity.getBody().contains("size는 1~100 사이의 값이어야 합니다.")).isTrue();
    }

    @Test
    @Transactional
    void testCreateOptions() {
        //given
        var requestOptionCreateDto = List.of(
            new OptionRequest.Create("option1", 1),
            new OptionRequest.Create("option2", 2)
        );
        var url = "http://localhost:" + port + "/api/products/" + dummyProduct.getId() + "/options";
        var request = new HttpEntity<>(requestOptionCreateDto, headers);

        //when
        var response = restTemplate.postForEntity(url, request, Void.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        productRepository.saveProduct(dummyProduct);
    }

    @Test
    @Transactional
    void testUpdateOptions() {
        //given
        var requestOptionUpdateDto = List.of(
            new OptionRequest.Update(
                dummyProduct.getOptions().getFirst().getId(),
                "updatedOption",
                2
            )
        );
        var request = new HttpEntity<>(requestOptionUpdateDto, headers);
        var url = "http://localhost:" + port + "/api/products/" + dummyProduct.getId() + "/options";

        //when
        var response = restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertAll(
            () -> assertThat(productRepository.getProductById(dummyProduct.getId())
                .getOptions()
                .getFirst()
                .getName())
                .isEqualTo("updatedOption"),
            () -> assertThat(productRepository.getProductById(dummyProduct.getId())
                .getOptions()
                .getFirst()
                .getQuantity())
                .isEqualTo(2)
        );

        //rollback
        productRepository.saveProduct(dummyProduct);
    }

    @Test
    @Transactional
    void testDeleteOptions() {
        //given
        var url = "http://localhost:" + port + "/api/products/" + dummyProduct.getId() + "/options";
        var request = new HttpEntity<>(List.of(dummyProduct.getOptions().getFirst().getId()), headers);

        //when
        var response = restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(productRepository.getProductById(dummyProduct.getId()).getOptions().size()).isEqualTo(0);

        //rollback
        productRepository.saveProduct(dummyProduct);
    }

    @Test
    @Transactional
    void testSubtractOption() {
        //given
        OptionIn.Subtract optionInSubtract = new OptionIn.Subtract(
            dummyProduct.getOptions().getFirst().getId(),
            1
        );

        //when
        productService.subtractOption(optionInSubtract, dummyProduct.getId());

        //then
        assertThat(productRepository.getProductById(dummyProduct.getId())
            .getOptions()
            .getFirst()
            .getQuantity())
            .isEqualTo(9);
    }
}
