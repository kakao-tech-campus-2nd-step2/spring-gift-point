package gift.controller;//package gift.controller;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import gift.classes.RequestState.RequestStateDTO;
//import gift.classes.RequestState.RequestStatus;
//import gift.classes.RequestState.SecureRequestStateDTO;
//import gift.classes.RequestState.WishListRequestStateDTO;
//import gift.dto.MemberDto;
//import gift.dto.ProductDto;
//import gift.dto.RequestWishDto;
//import gift.services.ProductService;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
//
//
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@Transactional
//class WishControllerTest {
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    private ProductService productService;
//
////    private String token;
//
////    @BeforeEach
////    @Sql(scripts = "/reset-database.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
////    void setUp() {
////        // 회원 등록
////        var registerUrl = "http://localhost:" + port + "/api/member/register";
////        MemberDto memberDto = new MemberDto(null, "testemail", "password", "admin");
////
////        HttpEntity<MemberDto> registerRequest = new HttpEntity<>(memberDto);
////        ResponseEntity<RequestStateDTO> registerResponse = restTemplate.exchange(registerUrl, HttpMethod.POST, registerRequest, RequestStateDTO.class);
////        assertEquals(200, registerResponse.getStatusCodeValue());
////
////        // 로그인
////        var loginUrl = "http://localhost:" + port + "/api/member/login";
////        HttpEntity<MemberDto> loginRequest = new HttpEntity<>(memberDto);
////        ResponseEntity<SecureRequestStateDTO> loginResponse = restTemplate.exchange(loginUrl, HttpMethod.POST, loginRequest, SecureRequestStateDTO.class);
////        assertEquals(200, loginResponse.getStatusCodeValue());
////        token = loginResponse.getBody().getSecure();
//
////        // 제품 추가
////        ProductDto productDto = new ProductDto(null, "Product1", 100.0, "imageUrl");
////        productService.addProduct(productDto);
////    }
//
//    @Test
//    @Order(1)
//    void addWishTest() {
//        // 회원 등록
//        var registerUrl = "http://localhost:" + port + "/api/member/register";
//        MemberDto memberDto = new MemberDto(null, "testemail1", "password", "admin");
//
//        HttpEntity<MemberDto> registerRequest = new HttpEntity<>(memberDto);
//        ResponseEntity<RequestStateDTO> registerResponse = restTemplate.exchange(registerUrl, HttpMethod.POST, registerRequest, RequestStateDTO.class);
//        assertEquals(200, registerResponse.getStatusCodeValue());
//
//        // 로그인
//        var loginUrl = "http://localhost:" + port + "/api/member/login";
//        HttpEntity<MemberDto> loginRequest = new HttpEntity<>(memberDto);
//        ResponseEntity<SecureRequestStateDTO> loginResponse = restTemplate.exchange(loginUrl, HttpMethod.POST, loginRequest, SecureRequestStateDTO.class);
//        assertEquals(200, loginResponse.getStatusCodeValue());
//        String token = loginResponse.getBody().getSecure();
//
//        // 제품 추가
//        ProductDto productDto = new ProductDto(null, "Product1", 100.0, "imageUrl");
//        ProductDto addedProductDto = productService.addProduct(productDto);
//
//        var url = "http://localhost:" + port + "/api/wishlist";
//        RequestWishDto requestWishDto = new RequestWishDto(addedProductDto.getId());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("authorization", "Bearer " + token);
//
//        HttpEntity<RequestWishDto> request = new HttpEntity<>(requestWishDto, headers);
//
//        ResponseEntity<RequestStateDTO> response = restTemplate.exchange(url, HttpMethod.POST, request, RequestStateDTO.class);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(RequestStatus.success, response.getBody().getStatus());
//    }
//
//    @Test
//    @Order(2)
//    void getWishlistByIdTest() {
//        var url = "http://localhost:" + port + "/api/wishlist";
//        // 회원 등록
//        var registerUrl = "http://localhost:" + port + "/api/member/register";
//        MemberDto memberDto = new MemberDto(null, "testemail2", "password", "admin");
//
//        HttpEntity<MemberDto> registerRequest = new HttpEntity<>(memberDto);
//        ResponseEntity<RequestStateDTO> registerResponse = restTemplate.exchange(registerUrl, HttpMethod.POST, registerRequest, RequestStateDTO.class);
//        assertEquals(200, registerResponse.getStatusCodeValue());
//
//        // 로그인
//        var loginUrl = "http://localhost:" + port + "/api/member/login";
//        HttpEntity<MemberDto> loginRequest = new HttpEntity<>(memberDto);
//        ResponseEntity<SecureRequestStateDTO> loginResponse = restTemplate.exchange(loginUrl, HttpMethod.POST, loginRequest, SecureRequestStateDTO.class);
//        assertEquals(200, loginResponse.getStatusCodeValue());
//        String token = loginResponse.getBody().getSecure();
//
////        제품 추가
//        ProductDto productDto = new ProductDto(null, "Product2", 100.0, "imageUrl");
//        ProductDto addedProductDto = productService.addProduct(productDto);
//
////        add Wish
//        RequestWishDto requestWishDto = new RequestWishDto(addedProductDto.getId());
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("authorization", "Bearer " + token);
//        HttpEntity<RequestWishDto> addrequest = new HttpEntity<>(requestWishDto, headers);
//        ResponseEntity<RequestStateDTO> addresponse = restTemplate.exchange(url, HttpMethod.POST, addrequest, RequestStateDTO.class);
//        assertEquals(200, addresponse.getStatusCodeValue());
//
////        Id로 Wishlist 가져오기
//        HttpEntity<Void> request = new HttpEntity<>(headers);
//        ResponseEntity<WishListRequestStateDTO> response = restTemplate.exchange(url, HttpMethod.GET, request, WishListRequestStateDTO.class);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        assertFalse(response.getBody().getWishes().isEmpty());
//    }
//
//    @Test
//    @Order(3)
//    void deleteWishTest() {
//
//        // 회원 등록
//        var registerUrl = "http://localhost:" + port + "/api/member/register";
//        MemberDto memberDto = new MemberDto(null, "testemail3", "password", "admin");
//
//        HttpEntity<MemberDto> registerRequest = new HttpEntity<>(memberDto);
//        ResponseEntity<RequestStateDTO> registerResponse = restTemplate.exchange(registerUrl, HttpMethod.POST, registerRequest, RequestStateDTO.class);
//        assertEquals(200, registerResponse.getStatusCodeValue());
//
//        // 로그인
//        var loginUrl = "http://localhost:" + port + "/api/member/login";
//        HttpEntity<MemberDto> loginRequest = new HttpEntity<>(memberDto);
//        ResponseEntity<SecureRequestStateDTO> loginResponse = restTemplate.exchange(loginUrl, HttpMethod.POST, loginRequest, SecureRequestStateDTO.class);
//        assertEquals(200, loginResponse.getStatusCodeValue());
//        String token = loginResponse.getBody().getSecure();
//
//        var url = "http://localhost:" + port + "/api/wishlist";
//
//        // 제품 추가
//        ProductDto productDto = new ProductDto(null, "Product3", 100.0, "imageUrl");
//        ProductDto addedProductDto = productService.addProduct(productDto);
//
//        // 위시 추가
//        RequestWishDto addRequestWishDto = new RequestWishDto(addedProductDto.getId());
//        HttpHeaders addHeaders = new HttpHeaders();
//        addHeaders.set("Authorization", "Bearer " + token);
//        HttpEntity<RequestWishDto> addRequest = new HttpEntity<>(addRequestWishDto, addHeaders);
//        ResponseEntity<RequestStateDTO> addResponse = restTemplate.exchange(url, HttpMethod.POST, addRequest, RequestStateDTO.class);
//        assertEquals(200, addResponse.getStatusCodeValue());
//
//        // 위시 삭제
//        RequestWishDto requestWishDto = new RequestWishDto(addedProductDto.getId());
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        HttpEntity<RequestWishDto> request = new HttpEntity<>(requestWishDto, headers);
//        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);
//
//        assertEquals(200, response.getStatusCodeValue());
//    }
//
//}