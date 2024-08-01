package gift.service.impl;

import gift.model.Member;
import gift.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member testMember;

    private final String secretKey = "ThisIsASecretKeyForJwtTokenThatIsVerySecure1234567890";

    @BeforeEach
    public void setUp() throws Exception {
        testMember = new Member();
        testMember.setId(1L);
        testMember.setEmail("test@example.com");
        testMember.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));

        Field secretKeyField = MemberServiceImpl.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(memberService, secretKey);
    }

    @Test
    public void testRegisterMember_Success() {
        given(memberRepository.save(any(Member.class))).willReturn(testMember);

        Optional<String> tokenOptional = memberService.registerMember(testMember);

        assertThat(tokenOptional).isPresent();
        String token = tokenOptional.get();

        Claims claims = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
            .build()
            .parseSignedClaims(token)
            .getPayload();

        assertThat(claims.getSubject()).isEqualTo(testMember.getId().toString());
        assertThat(claims.get("email")).isEqualTo(testMember.getEmail());
    }

    @Test
    public void testLogin_Success() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(testMember));

        Optional<String> tokenOptional = memberService.login("test@example.com", "password");

        assertThat(tokenOptional).isPresent();
        String token = tokenOptional.get();

        Claims claims = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
            .build()
            .parseSignedClaims(token)
            .getPayload();

        assertThat(claims.getSubject()).isEqualTo(testMember.getId().toString());
        assertThat(claims.get("email")).isEqualTo(testMember.getEmail());
    }

    @Test
    public void testLogin_InvalidPassword() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(testMember));

        Optional<String> tokenOptional = memberService.login("test@example.com", "wrongpassword");

        assertThat(tokenOptional).isNotPresent();
    }

    @Test
    public void testLogin_EmailNotFound() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Optional<String> tokenOptional = memberService.login("notfound@example.com", "password");

        assertThat(tokenOptional).isNotPresent();
    }

    @Test
    public void testFindById_Success() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(testMember));

        Optional<Member> foundMember = memberService.findById(1L);

        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo(testMember.getEmail());
    }

    @Test
    public void testFindById_NotFound() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Member> foundMember = memberService.findById(999L);

        assertThat(foundMember).isNotPresent();
    }
}
