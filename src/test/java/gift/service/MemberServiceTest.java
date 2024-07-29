package gift.service;

import static org.assertj.core.api.Assertions.assertThat;

import gift.common.util.JwtUtil;
import gift.dto.MemberRequest;
import gift.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    private MemberService memberService;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        memberService = new MemberService(memberRepository, jwtUtil);
    }

    @Test
    @DisplayName("회원 가입")
    void testRegisterMember() {
        // given
        MemberRequest memberRequest = new MemberRequest("user2@example.com", "password");

        // when
        String token = memberService.register(memberRequest);

        // then
        assertThat(token).isNotNull();
        assertThat(memberRepository.findByEmail("user2@example.com").get().getEmail()).isEqualTo(
            memberRequest.getEmail());

    }

    @Test
    @DisplayName("회원 로그인")
    void testLoginMember() {
        // given
        MemberRequest memberRequest = new MemberRequest("user2@example.com", "password");
        memberService.register(memberRequest);

        // when
        String token = memberService.login(memberRequest.getEmail(), memberRequest.getPassword());

        // then
        assertThat(token).isNotNull();
    }

}