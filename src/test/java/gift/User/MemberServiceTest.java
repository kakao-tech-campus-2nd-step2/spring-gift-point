package gift.User;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.Member.JpaMemberRepository;
import gift.domain.Member.Member;
import gift.domain.Member.MemberService;
import gift.domain.Member.dto.LoginInfo;
import gift.domain.Member.dto.MemberDTO;
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
    private MemberDTO memberDTO;

    @BeforeEach
    void setUp() {
        member = new Member("minji@example.com", "password1");
        memberDTO = new MemberDTO("minji@example.com", "password1");
    }

    @Test
    @Description("회원 가입")
    public void join() {
        // when
        memberService.join(memberDTO);
        Member findMember = memberRepository.findByEmail(memberDTO.getEmail());

        // then
        assertThat(findMember.getEmail()).isEqualTo(memberDTO.getEmail());
        assertThat(findMember.getPassword()).isEqualTo(memberDTO.getPassword());
    }

    @Test
    @Description("로그인")
    public void login() {
        // given
        Member savedMember = memberRepository.saveAndFlush(member);
        // when
        String token = memberService.login(memberDTO);
        LoginInfo loginInfo = jwtProvider.getLoginInfo(token);
        // then
        assertThat(loginInfo.getId()).isEqualTo(savedMember.getId());
        assertThat(loginInfo.getEmail()).isEqualTo(memberDTO.getEmail());
    }
}
