package gift.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.jwt.JwtToken;
import gift.domain.user.dto.UserRequest;
import gift.domain.user.dto.UserLoginRequest;
import gift.domain.user.service.UserService;
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
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    private static final String REGISTER_URL = "/api/users/register";
    private static final String LOGIN_URL = "/api/users/login";


    private MockHttpServletRequestBuilder postRequest(String url, String content) {
        return post(url)
            .content(content)
            .contentType(MediaType.APPLICATION_JSON);
    }


    @Test
    @DisplayName("회원 가입에 성공하는 경우")
    void create_success() throws Exception {
        // given
        UserRequest userRequest = new UserRequest("testUser", "test@test.com", "test123");
        String jsonContent = objectMapper.writeValueAsString(userRequest);

        JwtToken expectedJwtToken = new JwtToken("token");

        given(userService.signUp(any(UserRequest.class))).willReturn(expectedJwtToken);

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
        UserRequest userRequest = new UserRequest("testUser", "test@test.com", "test123");
        String jsonContent = objectMapper.writeValueAsString(userRequest);

        given(userService.signUp(any(UserRequest.class))).willThrow(DuplicateKeyException.class);

        // when & then
        mockMvc.perform(postRequest(REGISTER_URL, jsonContent))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("중복된 이메일입니다."));
    }

    @Test
    @DisplayName("로그인에 성공하는 경우")
    void login_success() throws Exception {
        // given
        UserLoginRequest userLoginRequest = new UserLoginRequest("test@test.com", "test123");
        String jsonContent = objectMapper.writeValueAsString(userLoginRequest);

        JwtToken expectedJwtToken = new JwtToken("token");

        given(userService.login(any(UserLoginRequest.class))).willReturn(expectedJwtToken);

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
        UserLoginRequest userLoginRequest = new UserLoginRequest("test@test.com", "test123");
        String jsonContent = objectMapper.writeValueAsString(userLoginRequest);

        given(userService.login(any(UserLoginRequest.class)))
            .willThrow(new InvalidUserInfoException("error.invalid.userinfo.password"));

        // when & then
        mockMvc.perform(postRequest(LOGIN_URL, jsonContent))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("잘못된 비밀번호입니다."));
    }
}