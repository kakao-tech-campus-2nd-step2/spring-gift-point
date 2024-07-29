package gift;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.KakaoToken;
import gift.domain.Member;
import gift.dto.TokenResponse;
import gift.repository.KakaoTokenRepository;
import gift.service.KakaoService;
import gift.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class KakaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private KakaoService kakaoLoginService;

    @MockBean
    private KakaoTokenRepository kakaoTokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Object> userInfo;

    @BeforeEach
    void setUp() {
        userInfo = new HashMap<>();
        userInfo.put("id", "12345");
        userInfo.put("kakao_account", new HashMap<String, Object>());
    }

    @Test
    @DisplayName("카카오 로그인 테스트")
    void kakaoLoginTest() throws Exception {
        String code = "authorization_code";
        String accessToken = "access_token";
        String email = "kakao_12345@kakao.com";
        String token = "jwt_token";

        Mockito.when(kakaoLoginService.getAccessToken(code)).thenReturn(accessToken);
        Mockito.when(kakaoLoginService.getUserInfo(accessToken)).thenReturn(userInfo);
        Mockito.when(memberService.findByEmail(email)).thenReturn(null);
        Mockito.when(memberService.generateTemporaryPassword()).thenReturn("temporary_password");

        Member member = new Member(null, email, "temporary_password");
        Mockito.when(memberService.register(Mockito.any(Member.class))).thenReturn(member);
        Mockito.when(memberService.generateToken(Mockito.any(Member.class))).thenReturn(token);

        Mockito.doAnswer(invocation -> {
            KakaoToken kakaoToken = invocation.getArgument(0);
            kakaoToken.setId(1L);
            return null;
        }).when(kakaoTokenRepository).save(Mockito.any(KakaoToken.class));

        mockMvc.perform(post("/kakao/login")
                        .param("code", code)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new TokenResponse(token))));
    }

}
