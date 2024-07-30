package gift.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.request.MemberRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("멤버 회원가입, 로그인 기능 테스트")
class MemberTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("회원가입")
    void registerMember() throws Exception {
        //Given
        MemberRequest registerRequest = new MemberRequest("member1@gmail.com", "1234");
        String requestJson = objectMapper.writeValueAsString(registerRequest);

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                //Then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("token").exists()
                );
    }

    @Test
    @Order(2)
    @DisplayName("로그인 성공")
    void loginMember() throws Exception {
        //Given
        MemberRequest request = new MemberRequest("member1@gmail.com", "1234");
        String requestJson = objectMapper.writeValueAsString(request);

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                //Then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("token").exists()
                );
    }

    @Test
    @Order(2)
    @DisplayName("로그인 실패 - 틀린 이메일")
    void loginMemberFail() throws Exception {
        //Given
        MemberRequest request = new MemberRequest("noResgietEmail@naver.com", "1234");
        String requestJson = objectMapper.writeValueAsString(request);

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                //Then
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
                );
    }

    @Test
    @Order(2)
    @DisplayName("로그인 실패 - 비번 틀림")
    void loginFail() throws Exception {
        //Given
        MemberRequest wrongInfoRequest = new MemberRequest("member1@gmail.com", "999999");
        String requestJson = objectMapper.writeValueAsString(wrongInfoRequest);

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                //Then
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
                        jsonPath("title").value("Member not found")
                );
    }

    @Test
    @Order(3)
    @DisplayName("사용중인 이메일로 회원가입")
    void duplicatedEmailRegister() throws Exception {
        //Given
        MemberRequest duplicatedEmailRegisterRequest = new MemberRequest("member1@gmail.com", "1234");
        String requestJson = objectMapper.writeValueAsString(duplicatedEmailRegisterRequest);

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                //Then
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
                        jsonPath("title").value("member1@gmail.com already in use")
                );
    }
}
