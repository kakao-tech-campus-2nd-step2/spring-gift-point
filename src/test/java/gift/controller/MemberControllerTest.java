package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.member.dto.MemberRequest;
import gift.domain.member.entity.Member;
import gift.domain.member.service.MemberService;
import java.net.URI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.sql.init.mode=never"
})
class MemberControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemberService memberService;

    private String token;

    @BeforeEach
    public void beforeEach() {
        // 회원 가입
        MemberRequest memberRequest = new MemberRequest("test@google.co.kr", "password");
        token = memberService.register(memberRequest);
    }

    @AfterEach
    public void afterEach() {
        // 유저 삭제
        Member member = memberService.getMemberFromToken(token);
        memberService.deleteMember(member.getId());
    }

    @Test
    @DisplayName("로그인 성공 E2E 테스트")
    void loginSuccessTest() {
        var request = new MemberRequest("test@google.co.kr", "password");
        var url = "http://localhost:" + port + "/api/members/login";
        var requestEntity = new RequestEntity<>(request, HttpMethod.POST, URI.create(url));

        var actual = restTemplate.exchange(requestEntity, String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("로그인 실패 E2E 테스트")
    void loginFailTest() {
        var request = new MemberRequest("test@google.co.kr", "wrong-password");
        var url = "http://localhost:" + port + "/api/members/login";
        var requestEntity = new RequestEntity<>(request, HttpMethod.POST, URI.create(url));

        var actual = restTemplate.exchange(requestEntity, String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}