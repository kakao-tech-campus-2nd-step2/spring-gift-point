package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.kakao.KakaoProperties;
import gift.dto.OAuthLoginRequest;
import gift.model.Member;
import gift.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OAuthServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private KakaoProperties kakaoProperties;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OAuthService oAuthService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("카카오 로그인 회원 가입 성공")
    void oauth_회원가입_성공() {
        OAuthLoginRequest request = new OAuthLoginRequest("kakao_test_id", "access_token");
        Member member = new Member(request.id(), "password");

        when(memberRepository.save(any(Member.class))).thenReturn(member);

        Member result = oAuthService.register(request);

        assertEquals(member, result);
    }
}
