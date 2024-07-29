package gift.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.auth.LoginRequest;
import gift.dto.wishproduct.WishProductAddRequest;
import gift.dto.wishproduct.WishProductResponse;
import gift.dto.wishproduct.WishProductUpdateRequest;
import gift.service.WishProductService;
import gift.service.auth.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WishProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WishProductService wishProductService;
    @Autowired
    private AuthService authService;
    private String managerToken;
    private String memberToken;


    @BeforeEach
    @DisplayName("관리자, 이용자의 토큰 값 세팅하기")
    void setBaseData() {
        var managerLoginRequest = new LoginRequest("admin@naver.com", "password");
        managerToken = authService.login(managerLoginRequest).token();
        var memberLoginRequest = new LoginRequest("member@naver.com", "password");
        memberToken = authService.login(memberLoginRequest).token();
    }

    @Test
    @DisplayName("잘못된 수량으로 된 위시 리스트 상품 추가하기")
    void failAddWishProductWithZeroCount() throws Exception {
        //given
        var postRequest = post("/api/wishes/add")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + memberToken)
                .content(objectMapper.writeValueAsString(new WishProductAddRequest(1L, 0)));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().string("상품의 수량은 반드시 1개 이상이어야 합니다."));
    }

    @Test
    @DisplayName("위시 리스트 상품 추가하기")
    void successAddWishProduct() throws Exception {
        //given
        var postRequest = post("/api/wishes/add")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + memberToken)
                .content(objectMapper.writeValueAsString(new WishProductAddRequest(1L, 10)));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        result.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("위시 리스트 상품 조회하기")
    void successGetWishProducts() throws Exception {
        //given
        var wishProduct = wishProductService
                .addWishProduct(new WishProductAddRequest(1L, 10), 1L);
        var getRequest = get("/api/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + memberToken);
        //when
        var readResult = mockMvc.perform(getRequest);
        //then
        var wishResult = readResult.andExpect(status().isOk()).andReturn();
        var wishResponseContent = wishResult.getResponse().getContentAsString();
        var wishProducts = objectMapper.readValue(wishResponseContent, new TypeReference<List<WishProductResponse>>() {
        });
        Assertions.assertThat(wishProducts.size()).isEqualTo(1);

        wishProductService.deleteWishProduct(wishProduct.id());
    }

    @Test
    @DisplayName("이미 위시 리스트에 저장된 상품 추가로 저장시 수량이 늘어난다")
    void successAddWishProductAlreadyExists() throws Exception {
        //given
        var wishProductAddRequest = new WishProductAddRequest(1L, 10);
        var wishProduct = wishProductService.addWishProduct(wishProductAddRequest, 1L);
        var postRequest = post("/api/wishes/add")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + memberToken)
                .content(objectMapper.writeValueAsString(wishProductAddRequest));
        //when
        var addResult = mockMvc.perform(postRequest);
        //then
        addResult.andExpect(status().isCreated());
        var wishProducts = wishProductService.getWishProducts(1L, PageRequest.of(0, 10));
        Assertions.assertThat(wishProducts.size()).isEqualTo(1);
        Assertions.assertThat(wishProducts.get(0).quantity()).isEqualTo(20);

        wishProductService.deleteWishProduct(wishProduct.id());
    }

    @Test
    @DisplayName("이용자끼리의 위시리스트가 다르다")
    void successGetDifferentWishProducts() throws Exception {
        //given
        var wishProduct1AddRequest = new WishProductAddRequest(1L, 10);
        var wishProduct2AddRequest = new WishProductAddRequest(2L, 10);
        wishProductService.addWishProduct(wishProduct1AddRequest, 1L);
        wishProductService.addWishProduct(wishProduct2AddRequest, 1L);
        var getRequest = get("/api/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + managerToken);
        //when
        var managerReadResult = mockMvc.perform(getRequest);
        //then
        var managerWishResult = managerReadResult.andExpect(status().isOk()).andReturn();
        var managerWishLength = managerWishResult.getResponse().getContentLength();
        Assertions.assertThat(managerWishLength).isEqualTo(0);
        var memberWishProducts = wishProductService.getWishProducts(1L, PageRequest.of(0, 10));
        Assertions.assertThat(memberWishProducts.size()).isEqualTo(2);

        deleteWishProducts(memberWishProducts);
    }

    @Test
    @DisplayName("위시 리스트 수량 변경하기")
    void successUpdateQuantity() throws Exception {
        //given
        var wishProduct = wishProductService
                .addWishProduct(new WishProductAddRequest(1L, 10), 1L);
        var putRequest = put("/api/wishes/update/" + wishProduct.id())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + memberToken)
                .content(objectMapper.writeValueAsString(new WishProductUpdateRequest(30)));
        //when
        var result = mockMvc.perform(putRequest);
        //then
        result.andExpect(status().isNoContent());
        var wishProducts = wishProductService.getWishProducts(1L, PageRequest.of(0, 10));
        Assertions.assertThat(wishProducts.size()).isEqualTo(1);
        Assertions.assertThat(wishProducts.get(0).quantity()).isEqualTo(30);

        wishProductService.deleteWishProduct(wishProduct.id());
    }

    @Test
    @DisplayName("위시 리스트 상품 추가후 수량 0으로 변경하기")
    void successUpdateQuantityZero() throws Exception {
        //given
        var wishProduct = wishProductService
                .addWishProduct(new WishProductAddRequest(1L, 10),
                        1L);
        var putRequest = put("/api/wishes/update/" + wishProduct.id())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + memberToken)
                .content(objectMapper.writeValueAsString(new WishProductUpdateRequest(0)));
        //when
        var result = mockMvc.perform(putRequest);
        //then
        result.andExpect(status().isNoContent());
        var wishProducts = wishProductService.getWishProducts(1L, PageRequest.of(0, 10));
        Assertions.assertThat(wishProducts.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("잘못된 정렬 데이터가 올 경우 예외를 던진다.")
    void failGetWishProductsWithInvalidPageSort() throws Exception {
        //given
        var getRequest = get("/api/wishes?sort=wrong,asc")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + memberToken);
        //when
        var getResult = mockMvc.perform(getRequest);
        //then
        getResult.andExpect(status().isBadRequest());
    }

    private void deleteWishProducts(List<WishProductResponse> wishProductResponses) {
        for (var wishProductResponse : wishProductResponses) {
            wishProductService.deleteWishProduct(wishProductResponse.id());
        }
    }
}
