package gift.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.service.MemberService;
import gift.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    JwtUtil jwtUtil;

    @Test
    void register() throws Exception {
        String requestBody = """
                {
                    "email" : "1234@1234.com",
                    "password" : "1234"
                }
                """;

        doNothing().when(memberService).register(any());
        mockMvc.perform(MockMvcRequestBuilders.post("/members/register").content(requestBody).contentType(
                MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void login() throws Exception {
        String requestBody = """
                {
                    "email" : "1234@1234.com",
                    "password" : "1234"
                }
                """;

        doNothing().when(memberService).login(any());
        mockMvc.perform(MockMvcRequestBuilders.post("/members/login").content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}