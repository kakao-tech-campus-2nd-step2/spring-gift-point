//package gift.e2e;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import gift.DTO.ProductDTO;
//import jakarta.transaction.Transactional;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.annotation.DirtiesContext;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Transactional
//public class WishListTest {
//
//    private static final String TEST_EMAIL = "test@example.com";
//    private static final String TEST_PASSWORD = "password123";
//    private static final int PRODUCT_COUNT = 11;
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    private String baseUrl;
//    private String authToken;
//    private List<ProductDTO> productList;
//
//    /**
//     * 테스트 설정을 초기화합니다.
//     */
//    @BeforeEach
//    void setUp() {
//        baseUrl = "http://localhost:" + port;
//        authToken = getToken();
//        productList = createProducts();
//    }
//
//    /**
//     * 테스트 용도의 인증 토큰을 생성합니다.
//     *
//     * @return 인증 토큰 문자열
//     */
//    private String getToken() {
//        String token = TestUtils.signup(restTemplate, baseUrl, TEST_EMAIL, TEST_PASSWORD);
//        return token != null ? token
//            : TestUtils.login(restTemplate, baseUrl, TEST_EMAIL, TEST_PASSWORD);
//    }
//
//    /**
//     * 테스트 용도의 상품 리스트를 생성합니다.
//     *
//     * @return 생성된 ProductDTO 객체 리스트
//     */
//    private List<ProductDTO> createProducts() {
//        String addProductUrl = baseUrl + "/api/products";
//        List<ProductDTO> products = new ArrayList<>();
//
//        for (int i = 1; i <= PRODUCT_COUNT; i++) {
//            ProductDTO product = new ProductDTO(null, "Product " + i, 1000 * i,
//                "https://example.com/image" + i);
//            ResponseEntity<ProductDTO> response = TestUtils.sendRequest(
//                restTemplate,
//                addProductUrl,
//                HttpMethod.POST,
//                product,
//                authToken,
//                ProductDTO.class
//            );
//            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//            products.add(response.getBody());
//        }
//
//        return products;
//    }
//
//    /**
//     * 모든 상품을 위시리스트에 추가합니다.
//     */
//    private void addAllProductsToWishlist() {
//        for (ProductDTO product : productList) {
//            TestUtils.sendRequest(
//                restTemplate,
//                baseUrl + "/api/wishlist",
//                HttpMethod.POST,
//                null,
//                authToken,
//                new ParameterizedTypeReference<ProductDTO>() {
//                },
//                "productId=" + product.id()
//            );
//        }
//    }
//
//    @Test
//    @DisplayName("위시리스트에 상품을 추가하는 테스트")
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
//    void addProductToWishlist() {
//        for (ProductDTO product : productList) {
//            ResponseEntity<ProductDTO> response = TestUtils.sendRequest(
//                restTemplate,
//                baseUrl + "/api/wishlist",
//                HttpMethod.POST,
//                null,
//                authToken,
//                new ParameterizedTypeReference<>() {
//                },
//                "productId=" + product.id()
//            );
//            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//            assertThat(response.getBody()).isEqualTo(product);
//        }
//    }
//
//    @Test
//    @DisplayName("위시리스트를 조회하는 테스트")
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
//    void viewWishlist() {
//        addAllProductsToWishlist();
//
//        ResponseEntity<List<ProductDTO>> response = TestUtils.sendRequest(
//            restTemplate,
//            baseUrl + "/api/wishlist/all",
//            HttpMethod.GET,
//            null,
//            authToken,
//            new ParameterizedTypeReference<>() {
//            }
//        );
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isNotEmpty();
//    }
//
//    @Test
//    @DisplayName("위시리스트를 페이징하여 조회하는 테스트")
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
//    void getWishlistWithPagination() {
//        addAllProductsToWishlist();
//
//        int page = 0;
//        int size = 10;
//        String criteria = "createdAt";
//        String direction = "desc";
//
//        ResponseEntity<List<ProductDTO>> response = TestUtils.sendRequest(
//            restTemplate,
//            baseUrl + "/api/wishlist?page=" + page + "&size=" + size + "&criteria=" + criteria
//                + "&direction=" + direction,
//            HttpMethod.GET,
//            null,
//            authToken,
//            new ParameterizedTypeReference<>() {
//            }
//        );
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isNotNull();
//        assertThat(response.getBody().size()).isEqualTo(size);
//    }
//
//    @Test
//    @DisplayName("위시리스트에서 상품을 삭제하는 테스트")
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
//    void deleteProductFromWishlist() {
//        addAllProductsToWishlist();
//        ProductDTO productToDelete = productList.get(1);
//
//        ResponseEntity<Void> deleteResponse = TestUtils.sendRequest(
//            restTemplate,
//            baseUrl + "/api/wishlist/" + productToDelete.id(),
//            HttpMethod.DELETE,
//            null,
//            authToken,
//            Void.class
//        );
//        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//
//        ResponseEntity<List<ProductDTO>> getResponse = TestUtils.sendRequest(
//            restTemplate,
//            baseUrl + "/api/wishlist/all",
//            HttpMethod.GET,
//            null,
//            authToken,
//            new ParameterizedTypeReference<>() {
//            }
//        );
//        assertThat(getResponse.getBody()).doesNotContain(productToDelete);
//    }
//
//    @Test
//    @DisplayName("위시리스트의 모든 상품을 삭제하는 테스트")
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
//    void deleteAllWishlistItems() {
//        addAllProductsToWishlist();
//
//        ResponseEntity<Void> deleteResponse = TestUtils.sendRequest(
//            restTemplate,
//            baseUrl + "/api/wishlist/all",
//            HttpMethod.DELETE,
//            null,
//            authToken,
//            Void.class
//        );
//        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//
//        ResponseEntity<List<ProductDTO>> getResponse = TestUtils.sendRequest(
//            restTemplate,
//            baseUrl + "/api/wishlist/all",
//            HttpMethod.GET,
//            null,
//            authToken,
//            new ParameterizedTypeReference<>() {
//            }
//        );
//        assertThat(getResponse.getBody()).isEmpty();
//    }
//}
