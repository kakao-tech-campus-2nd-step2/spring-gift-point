package gift.controller;

import gift.dto.LoginMemberToken;
import gift.dto.MemberRequest;
import gift.dto.ProductDTO;
import gift.dto.WishListRequest;
import java.util.HashMap;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WishListControllerTest {

    @LocalServerPort
    private int port;
    WebTestClientHelper webClient;

    @BeforeEach
    void setUp() {
        webClient = new WebTestClientHelper(port);
    }

    @Test
    @DisplayName("위시 리스트 아이템 추가")
    void addWishList() {
        //given
        //회원가입 and 로그인
        String email = "abec";
        String password = "abecdddd";
        LoginMemberToken loginMemberToken = registerAndLogin(email, password);

        //상품 추가
        webClient.post("api/products",new ProductDTO(null,"test",123,"abc",1L));

        ProductDTO dto =  webClient.get("api/products").expectBodyList(ProductDTO.class).returnResult().getResponseBody()
            .get(0);

        webClient.moreAction().post().uri("api/wishlist")
            .header("Authorization", loginMemberToken.getToken())
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new WishListRequest(dto.getId(),1)))
            .exchange().expectStatus().isOk();


    }

    /*
    @Test
    @DisplayName("위시 리스트 조회")
    void getWishList() {

    }

    @Test
    @DisplayName("위시 리스트 아이템 수량 변경")
    void updateWishList() {

    }

    @Test
    @DisplayName("위시 리스트 아이템 삭제")
    void deleteWishList() {

    }


     */
    private LoginMemberToken registerAndLogin(String email, String password) {
        //register
        MemberRequest memberRequest = new MemberRequest(email, password, null);
        webClient.put("/api/member", memberRequest);

        //login
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("email", memberRequest.getEmail());
        userInfo.put("password", memberRequest.getPassword());
        String uri = webClient.uriMakeUseParameters("/api/member/login", userInfo);
        String token = Objects.requireNonNull(
            webClient.moreAction().get().uri(uri).accept(MediaType.APPLICATION_JSON).exchange()
                .expectBody(LoginMemberToken.class).returnResult().getResponseBody()).getToken();
        return new LoginMemberToken(token);
    }

}