package gift.unitTest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.WebConfig;
import gift.controller.auth.AuthController;
import gift.controller.auth.LoginRequest;
import gift.controller.auth.Token;
import gift.service.AuthService;
import gift.util.JwtUtil;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private WebConfig webConfig;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("로그인 - 성공")
//    @Test
    void loginTest() throws Exception {
        // given
        var id = UUID.randomUUID();
        var request = new LoginRequest("validEmail@kakao.com", "validPassword");
        var token = new Token(JwtUtil.generateToken(id, request.email()));
        when(authService.login(request)).thenReturn(token);

        // when
        mockMvc.perform(
                MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))

            // then
            .andExpect(status().isOk())
            .andExpect(header().string("Authorization", "Bearer " + token.token()))
            .andExpect(content().json(objectMapper.writeValueAsString(token)));
        verify(authService, times(1)).login(request);
    }
}
