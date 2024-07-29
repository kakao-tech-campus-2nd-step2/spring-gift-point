package gift.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.auth.LoginRequest;
import gift.dto.auth.RegisterRequest;
import gift.reflection.AuthTestReflectionComponent;
import gift.service.MemberService;
import gift.service.auth.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthTestReflectionComponent authTestReflectionComponent;

    @Test
    @DisplayName("빈 이름으로 회원가입 요청하기")
    void failRegisterWithEmptyName() throws Exception {
        //given
        var postRequest = post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RegisterRequest("", "test@naver.com", "testPassword", "MEMBER")));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().string("이름의 길이는 최소 1자 이상이어야 합니다."));
    }

    @Test
    @DisplayName("이름의 길이가 8초과인 이용자의 회원가입 요청하기")
    void failRegisterWithNameOverLength() throws Exception {
        //given
        var postRequest = post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RegisterRequest("이름이8글자초과예요", "test@naver.com", "testPassword", "MEMBER")));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().string("이름의 길이는 8자를 초과할 수 없습니다."));
    }

    @Test
    @DisplayName("허용되지 않는 형식의 이메일로 회원가입 요청하기")
    void failRegisterWithWrongEmailReg() throws Exception {
        //given
        var postRequest = post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RegisterRequest("테스트", "test@hello", "testPassword", "MEMBER")));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().string("허용되지 않은 형식의 이메일입니다."));
    }

    @Test
    @DisplayName("허용되지 않는 형식의 패스워드로 회원가입 요청하기")
    void failRegisterWithWrongPasswordReg() throws Exception {
        //given
        var postRequest = post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RegisterRequest("테스트", "test@naver.com", "잘못된패스워드", "MEMBER")));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().string("허용되지 않은 형식의 패스워드입니다."));
    }

    @Test
    @DisplayName("허용되지 않는 형식의 회원타입으로 회원가입 요청하기")
    void failRegisterWithWrongMemberRole() throws Exception {
        //given
        var postRequest = post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RegisterRequest("테스트", "test@naver.com", "testPassword", "MEMBERS")));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().string("존재하지 않는 회원 타입입니다."));
    }

    @Test
    @DisplayName("허용되지 않는 형식의 이메일로 로그인 요청하기")
    void failLoginWithWrongEmailReg() throws Exception {
        //given
        var postRequest = post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest("test@hello", "testPassword")));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().string("허용되지 않은 형식의 이메일입니다."));
    }

    @Test
    @DisplayName("허용되지 않는 형식의 패스워드로 로그인 요청하기")
    void failLoginWithWrongPasswordReg() throws Exception {
        //given
        var postRequest = post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest("test@naver.com", "잘못된패스워드")));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().string("허용되지 않은 형식의 패스워드입니다."));
    }

    @Test
    @DisplayName("정상적으로 회원가입 후 잘못된 패스워드로 로그인 요청하기")
    void failLoginWithWrongPassword() throws Exception {
        //given
        var auth = authService.register(new RegisterRequest("테스트", "test@naver.com", "testPassword", "MEMBER"));
        var postRequest = post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest("test@naver.com", "testPasswordWrong")));
        //when
        var login = mockMvc.perform(postRequest);
        //then
        login.andExpect(status().isUnauthorized());

        memberService.deleteMember(authTestReflectionComponent.getMemberIdWithToken(auth.token()));
    }

    @Test
    @DisplayName("정상적으로 회원가입 후 로그인 요청하기")
    void successLogin() throws Exception {
        //given
        var auth = authService.register(new RegisterRequest("테스트", "test@naver.com", "testPassword", "MEMBER"));
        var postRequest = post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest("test@naver.com", "testPassword")));
        //when
        var login = mockMvc.perform(postRequest);
        //then
        login.andExpect(status().isOk());

        memberService.deleteMember(authTestReflectionComponent.getMemberIdWithToken(auth.token()));
    }
}
