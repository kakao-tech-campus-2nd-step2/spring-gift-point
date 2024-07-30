package gift.service;

import gift.domain.Member;
import gift.domain.TokenAuth;
import gift.repository.token.TokenSpringDataJpaRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static gift.domain.LoginType.NORMAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private TokenSpringDataJpaRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    private final static Long MEMBER_ID = 1L;
    private final static String EMAIL = "test@example.com";
    private final static String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";


    @BeforeEach
    public void setup() {
        tokenService = new TokenService(tokenRepository, SECRET_KEY);
    }

    @Test
    public void testSaveToken() {
        Member member = new Member(EMAIL, "password", NORMAL);
        member.setId(MEMBER_ID);

        String token = tokenService.saveToken(member);
        assertNotNull(token);
    }

    @Test
    public void testFindToken() {
        Member member = new Member(EMAIL, "password", NORMAL);
        member.setId(MEMBER_ID);
        String token = tokenService.saveToken(member);

        TokenAuth tokenAuth = new TokenAuth(token, member);
        assertNotNull(tokenAuth.getToken());

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenAuth));

        TokenAuth foundToken = tokenService.findToken(token);

        assertNotNull(foundToken);
        assertEquals(EMAIL, foundToken.getMember().getEmail());
    }

//    @Test
//    public void testGetMemberIdFromToken() {
//        Member member = new Member(EMAIL, "password");
//        member.setId(MEMBER_ID);
//        String token = tokenService.saveToken(member);
//
//        String memberId = tokenService.getMemberIdFromToken(token);
//
//        assertEquals(MEMBER_ID.toString(), memberId);
//    }

}

