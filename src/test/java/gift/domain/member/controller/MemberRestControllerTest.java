package gift.domain.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.jwt.JwtToken;
import gift.domain.member.dto.MemberLoginRequest;
import gift.domain.member.dto.MemberRequest;
import gift.domain.member.service.MemberService;
import gift.exception.InvalidUserInfoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@AutoConfigureMockMvc
@SpringBootTest
class MemberRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;


    private static final String REGISTER_URL = "/api/members/register";
    private static final String LOGIN_URL = "/api/members/login";


    private MockHttpServletRequestBuilder postRequest(String url, String content) {
        return post(url)
            .content(content)
            .contentType(MediaType.APPLICATION_JSON);
    }


    @Test
    @DisplayName("회원 가입에 성공하는 경우")
    void create_success() throws Exception {
        // given
        MemberRequest memberRequest = new MemberRequest("testUser", "test@test.com", "test123");
        String jsonContent = objectMapper.writeValueAsString(memberRequest);

        JwtToken expectedJwtToken = new JwtToken("token");

        given(memberService.signUp(any(MemberRequest.class))).willReturn(expectedJwtToken);

        // when & then
        mockMvc.perform(postRequest(REGISTER_URL, jsonContent))
            .andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJwtToken)))
            .andDo(print());
    }

    @Test
    @DisplayName("회원 가입에 실패하는 경우 - 이미 존재하는 이메일로 가입 시도")
    void create_fail() throws Exception {
        // given
        MemberRequest memberRequest = new MemberRequest("testUser", "test@test.com", "test123");
        String jsonContent = objectMapper.writeValueAsString(memberRequest);

        given(memberService.signUp(any(MemberRequest.class))).willThrow(DuplicateKeyException.class);

        // when & then
        mockMvc.perform(postRequest(REGISTER_URL, jsonContent))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("중복된 이메일입니다."));
    }

    @Test
    @DisplayName("로그인에 성공하는 경우")
    void login_success() throws Exception {
        // given
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("test@test.com", "test123");
        String jsonContent = objectMapper.writeValueAsString(memberLoginRequest);

        JwtToken expectedJwtToken = new JwtToken("token");

        given(memberService.login(any(MemberLoginRequest.class))).willReturn(expectedJwtToken);

        // when & then
        mockMvc.perform(postRequest(LOGIN_URL, jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJwtToken)))
            .andDo(print());
    }

    @Test
    @DisplayName("로그인에 실패하는 경우 - 틀린 비밀번호로 로그인 시도")
    void login_fail() throws Exception {
        // given
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("test@test.com", "test123");
        String jsonContent = objectMapper.writeValueAsString(memberLoginRequest);

        given(memberService.login(any(MemberLoginRequest.class)))
            .willThrow(new InvalidUserInfoException("error.invalid.userinfo.password"));

        // when & then
        mockMvc.perform(postRequest(LOGIN_URL, jsonContent))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("잘못된 비밀번호입니다."));
    }
}