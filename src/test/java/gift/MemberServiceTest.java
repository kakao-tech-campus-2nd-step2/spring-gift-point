package gift;

import gift.model.Member;
import gift.repository.MemberRepository;
import gift.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        // 회원가입
        String email = "test@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";

        when(memberRepository.findByEmail(email)).thenReturn(null);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
            Member member = invocation.getArgument(0);
            return member;
        });

        Member member = memberService.register(email, password);

        assertThat(member).isNotNull();
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    void testRegisterThrowsExceptionWhenEmailExists() {
        // 중복 이메일 회원가입
        String email = "test@example.com";
        String password = "password";

        when(memberRepository.findByEmail(email)).thenReturn(new Member());

        assertThrows(IllegalArgumentException.class, () -> memberService.register(email, password));
    }

    @Test
    void testAuthenticate() {
        // 로그인
        String email = "test@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";

        Member member = new Member();
        member.setEmail(email);
        member.setPassword(encodedPassword);

        when(memberRepository.findByEmail(email)).thenReturn(member);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        Member authenticatedMember = memberService.authenticate(email, password);

        assertThat(authenticatedMember).isNotNull();
        assertThat(authenticatedMember.getEmail()).isEqualTo(email);
    }

    @Test
    void testAuthenticateReturnsNullWhenPasswordDoesNotMatch() {
        // 비밀번호 일치하지 않을 때 로그인 실패?
        String email = "test@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";

        Member member = new Member();
        member.setEmail(email);
        member.setPassword(encodedPassword);

        when(memberRepository.findByEmail(email)).thenReturn(member);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        Member authenticatedMember = memberService.authenticate(email, password);

        assertThat(authenticatedMember).isNull();
    }

    @Test
    void testFindById() {
        // ID로 회원 조회
        Long id = 1L;
        Member member = new Member();
        member.setEmail("test@example.com");
        member.setPassword("password");

        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        Member foundMember = memberService.findById(id);

        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testFindByIdReturnsNullWhenNotFound() {
        // 없는 ID로 회원 조회시 Null 반환
        Long id = 1L;

        when(memberRepository.findById(id)).thenReturn(Optional.empty());

        Member foundMember = memberService.findById(id);

        assertThat(foundMember).isNull();
    }
}