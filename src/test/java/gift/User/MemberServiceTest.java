package gift.User;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.member.JpaMemberRepository;
import gift.domain.member.Member;
import gift.domain.member.MemberService;
import gift.domain.member.dto.LoginInfo;
import gift.domain.member.dto.request.MemberRequest;
import gift.domain.member.dto.response.MemberResponse;
import gift.global.jwt.JwtProvider;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    JpaMemberRepository memberRepository;
    @Autowired
    JwtProvider jwtProvider;

    private Member member;
    private MemberRequest memberRequest;

    @BeforeEach
    void setUp() {
        member = new Member("minji@example.com", "password1");
        memberRequest = new MemberRequest("minji@example.com", "password1");
    }

    @Test
    @Description("회원 가입")
    public void join() {
        // when
        memberService.join(memberRequest);
        Member findMember = memberRepository.findByEmail(memberRequest.email());

        // then
        assertThat(findMember.getEmail()).isEqualTo(memberRequest.email());
        assertThat(findMember.getPassword()).isEqualTo(memberRequest.password());
    }

    @Test
    @Description("로그인")
    public void login() {
        // given
        Member savedMember = memberRepository.saveAndFlush(member);
        // when
        MemberResponse memberResponse = memberService.login(memberRequest);
        LoginInfo loginInfo = jwtProvider.getLoginInfo(memberResponse.accessToken());
        // then
        assertThat(loginInfo.getId()).isEqualTo(savedMember.getId());
        assertThat(loginInfo.getEmail()).isEqualTo(memberRequest.email());
    }
}
