package gift.controller;

import static gift.util.constants.MemberConstants.EMAIL_ALREADY_USED;
import static gift.util.constants.MemberConstants.INVALID_CREDENTIALS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.dto.member.MemberLoginRequest;
import gift.dto.member.MemberRegisterRequest;
import gift.dto.member.MemberResponse;
import gift.exception.member.EmailAlreadyUsedException;
import gift.exception.member.ForbiddenException;
import gift.service.MemberService;
import gift.util.TokenValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private TokenValidator tokenValidator;

    private MemberResponse memberResponse;

    @BeforeEach
    public void setUp() {
        memberResponse = new MemberResponse(1L, "test@example.com", "token");
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void testRegister() throws Exception {
        when(memberService.registerMember(any(MemberRegisterRequest.class))).thenReturn(
            memberResponse);

        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"password\": \"password\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("이미 사용 중인 이메일로 회원가입 시도")
    public void testRegisterEmailAlreadyUsed() throws Exception {
        when(memberService.registerMember(any(MemberRegisterRequest.class))).thenThrow(
            new EmailAlreadyUsedException(EMAIL_ALREADY_USED));

        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"password\": \"password\"}"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error").value(EMAIL_ALREADY_USED));
    }

    @Test
    @DisplayName("로그인 테스트")
    public void testLogin() throws Exception {
        when(memberService.loginMember(any(MemberLoginRequest.class))).thenReturn(memberResponse);

        mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"password\": \"password\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("잘못된 이메일로 로그인 시도")
    public void testLoginEmailNotFound() throws Exception {
        when(memberService.loginMember(any(MemberLoginRequest.class))).thenThrow(
            new ForbiddenException(INVALID_CREDENTIALS));

        mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"password\": \"password\"}"))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.error").value(INVALID_CREDENTIALS));
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시도")
    public void testLoginPasswordMismatch() throws Exception {
        when(memberService.loginMember(any(MemberLoginRequest.class))).thenThrow(
            new ForbiddenException(INVALID_CREDENTIALS));

        mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"password\": \"wrongpassword\"}"))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.error").value(INVALID_CREDENTIALS));
    }
}
