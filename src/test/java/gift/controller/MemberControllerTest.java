package gift.controller;

import gift.dto.LoginMemberToken;
import gift.dto.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

    @LocalServerPort
    private int port;
    private String baseUrl;
    private WebTestClient webClient;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        webClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();
    }

    @Test
    @DisplayName("회원가입 성공")
    void registerMember() {
        //given
        String email = "asdef@gmail.com";
        String password = "abcd";
        MemberRequest dto = new MemberRequest(email, password, null);

        //when
        ResponseSpec responseSpec = registerMemberPutRequest(dto);

        //then
        responseSpec.expectStatus().isOk();
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입 시도")
    void registerDuplicateEmail() {
        //given
        String email = "abcd@gmail.com";

        MemberRequest dto = new MemberRequest(email, "1234", null);
        MemberRequest dto2 = new MemberRequest(email, "4567", null);

        //when
        registerMemberPutRequest(dto);
        ResponseSpec responseSpec2 = registerMemberPutRequest(dto2);

        responseSpec2.expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("이메일을 입력하지 않은 경우")
    void nullEmail() {
        //given
        MemberRequest dto = new MemberRequest(null, "1234", null);

        //when
        ResponseSpec responseSpec = registerMemberPutRequest(dto);

        //then
        responseSpec.expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("패스워드를 입력하지 않은 경우")
    void nullPassword() {
        //given
        MemberRequest dto = new MemberRequest("abcd@abcd", null, null);

        //when
        ResponseSpec responseSpec = registerMemberPutRequest(dto);

        //then
        responseSpec.expectStatus().isBadRequest();
    }


    @Test
    @DisplayName("로그인 성공")
    void login() {
        //given
        String email = "abcd@gmail.com";
        String password = "abcd";
        MemberRequest dto = new MemberRequest(email, password, null);
        registerMemberPutRequest(dto);

        //when
        ResponseSpec responseSpec = webClient.get().uri(uriBuilder -> uriBuilder
            .path("/api/member/login")
            .queryParam("email", dto.getEmail())
            .queryParam("password", dto.getPassword())
            .build()).accept(MediaType.APPLICATION_JSON).exchange();

        //then
        responseSpec.expectStatus().isOk();
        responseSpec.expectBody(LoginMemberToken.class);
    }

    @Test
    @DisplayName("패스워드가 불일치한 경우")
    void wrongPassword() {
        //given
        String email = "abcd@gmail.com";
        String password = "abcd";
        String wrongPassword = "wrong";
        MemberRequest dto = new MemberRequest(email, password, null);
        registerMemberPutRequest(dto);

        //when
        ResponseSpec responseSpec = webClient.get().uri(uriBuilder -> uriBuilder
            .path("/api/member/login")
            .queryParam("email", dto.getEmail())
            .queryParam("password", wrongPassword)
            .build()).accept(MediaType.APPLICATION_JSON).exchange();

        //then
        responseSpec.expectStatus().isUnauthorized();

    }

    @Test
    @DisplayName("회원가입이 되지않은 이메일로 로그인을 시도하는 경우")
    void noRegister() {
        //given
        String email = "imno@gmail.com";
        String password = "abcd";
        MemberRequest dto = new MemberRequest(email, password, null);

        //when
        ResponseSpec responseSpec = webClient.get().uri(uriBuilder -> uriBuilder
            .path("/api/member/login")
            .queryParam("email", dto.getEmail())
            .queryParam("password", dto.getPassword())
            .build()).accept(MediaType.APPLICATION_JSON).exchange();

        //then
        responseSpec.expectStatus().isUnauthorized();
    }


    private ResponseSpec registerMemberPutRequest(MemberRequest dto) {
        ResponseSpec responseSpec = webClient.put().uri("/api/member")
            .accept(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(dto)).exchange();
        return responseSpec;
    }

}