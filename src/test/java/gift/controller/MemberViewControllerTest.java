package gift.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MemberViewControllerTest {

    private @Autowired MockMvc mockMvc;

    @Test
    @DisplayName("회원가입 폼 가져오기 테스트")
    void registerMemberForm() throws Exception {
        mockMvc.perform(get("/api/members/register"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 폼 가져오기 테스트")
    void loginForm() throws Exception {
        mockMvc.perform(get("/api/members/login"))
            .andExpect(status().isOk());
    }
}