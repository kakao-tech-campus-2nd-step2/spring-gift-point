package gift.ControllerTest;

import gift.service.KakaoLoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class KakaoLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KakaoLoginService kakaoLoginService;

    @BeforeEach
    void setUp() {
        given(kakaoLoginService.getUrl()).willReturn("http://localhost:8080/oauth/authorize");
        given(kakaoLoginService.getAccessToken(anyString())).willReturn("토큰");
        given(kakaoLoginService.signUpAndLogin(anyString())).willReturn("회원가입 및 로그인 되었습니다.");
    }

    @Test
    void testKakaoLogin() throws Exception {
        mockMvc.perform(get("/api/kakao/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:8080/oauth/authorize"));
    }

    @Test
    void testSignUpAndLogin() throws Exception {
        mockMvc.perform(get("/").param("code", "인가 코드"))
                .andExpect(status().isOk())
                .andExpect(content().string("회원가입 및 로그인 되었습니다."));
    }
}
