package gift.controller;

import gift.dto.auth.LoginRequest;
import gift.dto.auth.RegisterRequest;
import gift.service.auth.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("정상적으로 가입 후 탈퇴 요청하기")
    void successDeleteMember() throws Exception {
        //given
        var registerRequest = new RegisterRequest("테스트", "test@naver.com", "testPassword", "MEMBER");
        authService.register(registerRequest);
        var loginRequest = new LoginRequest("test@naver.com", "testPassword");
        var loginResponse = authService.login(loginRequest);
        var deleteRequest = delete("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + loginResponse.token());
        //when
        var deleted = mockMvc.perform(deleteRequest);
        //then
        deleted.andExpect(status().isNoContent());
    }
}
