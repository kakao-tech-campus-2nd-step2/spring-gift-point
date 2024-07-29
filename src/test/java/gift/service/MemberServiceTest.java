package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import gift.model.Member;
import gift.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock // 가짜 객체를 생성
    private MemberRepository memberRepository;

    @InjectMocks // 실제 객체를 생성하고 @Mock으로 생성된 객체들을 이 객체에 주입
    private MemberService memberService;

    private final String secretKey = "testsecretkeytestsecretkeytestsecretkey";

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("test@email.com", "testpassword");
        member.setId(1L);
        ReflectionTestUtils.setField(memberService, "secretKey", secretKey);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void registerMemberTest() throws Exception {
        // given
        String hashedPassword = BCrypt.hashpw(member.getPassword(), BCrypt.gensalt());
        member.setPassword(hashedPassword);
        when(memberRepository.save(member)).thenReturn(member);

        // when
        var token = memberService.registerMember(member);

        // then
        assertThat(token).isPresent();
        var actualToken = token.get();
        assertThat(Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseSignedClaims(actualToken)
            .getPayload()
            .getSubject())
            .isEqualTo(member.getId().toString());
    }

    @Test
    @DisplayName("로그인 테스트")
    public void loginMemberTest() throws Exception {
        // given
        String hashedPassword = BCrypt.hashpw(member.getPassword(), BCrypt.gensalt());
        member.setPassword(hashedPassword);
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

        // when
        var token = memberService.login(member.getEmail(), "testpassword");

        // then
        assertThat(token).isPresent();
        var actualToken = token.get();
        assertThat(Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseSignedClaims(actualToken)
            .getPayload()
            .getSubject()
        ).isEqualTo(member.getId().toString());
    }

    @Test
    @DisplayName("Id 찾기 테스트")
    public void findMemberByIdTest() throws Exception {
        // given
        when(memberRepository.findById(member.getId())).thenReturn(
            Optional.of(member));

        // when
        var foundMember = memberService.findById(member.getId());

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo(member.getEmail());
    }

}