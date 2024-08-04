package gift.member.presentation;

import gift.auth.KakaoService;
import gift.auth.TokenService;
import gift.member.application.MemberService;
import gift.member.application.command.MemberEmailUpdateCommand;
import gift.member.application.command.MemberPasswordUpdateCommand;
import gift.member.application.response.MemberLoginServiceResponse;
import gift.member.application.response.MemberRegisterServiceResponse;
import gift.member.application.response.MemberServiceResponse;
import gift.member.domain.Member;
import gift.member.domain.OauthProvider;
import gift.member.presentation.request.MemberLoginRequest;
import gift.member.presentation.request.MemberRegisterRequest;
import gift.member.presentation.request.PointUpdateRequest;
import gift.member.presentation.request.ResolvedMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private KakaoService kakaoService;

    private Long memberId;
    private String email;
    private String password;
    private String token;

    @BeforeEach
    void setUp() {
        memberId = 1L;
        email = "test@example.com";
        password = "password";
        token = "testToken";
        MockitoAnnotations.openMocks(this);

        when(tokenService.extractMemberId(eq(token))).thenReturn(memberId);
    }

    @Test
    void 회원가입_테스트() throws Exception {
        // Given
        MemberRegisterRequest request = new MemberRegisterRequest(email, password);
        MemberRegisterServiceResponse memberRegisterServiceResponse = MemberRegisterServiceResponse.from(new Member(
                1L,
                email,
                password,
                OauthProvider.COMMON));
        when(memberService.register(request.toCommand())).thenReturn(memberRegisterServiceResponse);
        when(tokenService.createToken(memberId)).thenReturn(token);

        // When & Then
        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    void 로그인_테스트() throws Exception {
        // Given
        MemberLoginRequest request = new MemberLoginRequest(email, password);
        MemberLoginServiceResponse memberLoginServiceResponse = MemberLoginServiceResponse.from(
                new Member(
                        1L,
                        email,
                        password,
                        OauthProvider.COMMON
                ));
        when(memberService.login(request.toCommand())).thenReturn(memberLoginServiceResponse);
        when(tokenService.createToken(memberLoginServiceResponse.id())).thenReturn(token);

        // When & Then
        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    void 아이디로_찾기_테스트() throws Exception {
        // Given
        MemberServiceResponse response = new MemberServiceResponse(memberId, email, password);
        when(memberService.findById(eq(memberId))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/members/{id}", memberId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.password").value(password));
    }

    @Test
    void 전체_회원_찾기_테스트() throws Exception {
        // Given
        MemberServiceResponse response1 = new MemberServiceResponse(memberId, email, password);
        MemberServiceResponse response2 = new MemberServiceResponse(2L, "test2@example.com", "password2");
        when(memberService.findAll()).thenReturn(Arrays.asList(response1, response2));

        // When & Then
        mockMvc.perform(get("/api/members")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(email))
                .andExpect(jsonPath("$[1].email").value("test2@example.com"));
    }

    @Test
    void 이메일_업데이트_테스트() throws Exception {
        // Given
        String newEmail = "test2@example.com";
        ResolvedMember resolvedMember = new ResolvedMember(memberId);
        MemberEmailUpdateCommand expectedCommand = new MemberEmailUpdateCommand(newEmail);

        MemberServiceResponse memberServiceResponse = new MemberServiceResponse(memberId, email, password);
        when(memberService.findById(anyLong())).thenReturn(memberServiceResponse);

        // When & Then
        mockMvc.perform(put("/api/members/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + newEmail + "\"}")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());

        verify(memberService).updateEmail(eq(resolvedMember.id()), (eq(expectedCommand)));
    }

    @Test
    void 비밀번호_업데이트_테스트() throws Exception {
        // Given
        String newPassword = "newPassword";
        ResolvedMember member = new ResolvedMember(memberId);
        MemberPasswordUpdateCommand updateCommand = new MemberPasswordUpdateCommand(newPassword);

        MemberServiceResponse memberServiceResponse = new MemberServiceResponse(memberId, email, password);
        when(memberService.findById(anyLong())).thenReturn(memberServiceResponse);

        // When & Then
        mockMvc.perform(put("/api/members/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"" + newPassword + "\"}")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());

        verify(memberService).updatePassword(eq(member.id()), eq(updateCommand));
    }

    @Test
    void 회원_삭제_테스트() throws Exception {
        // Given
        MemberServiceResponse memberServiceResponse = new MemberServiceResponse(memberId, email, password);
        ResolvedMember member = new ResolvedMember(memberId);
        when(memberService.findById(anyLong())).thenReturn(memberServiceResponse);

        // When & Then
        mockMvc.perform(delete("/api/members")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNoContent());

        verify(memberService).delete(eq(member.id()));
    }

    @Test
    void 카카오로그인_테스트() throws Exception {
        // Given
        String redirectUrl = "http://localhost:8080/api/member/login/kakao/callback";
        when(kakaoService.getKakaoRedirectUrl()).thenReturn(redirectUrl);

        // When & Then
        mockMvc.perform(get("/api/members/login/kakao"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectUrl));

        verify(kakaoService, times(1)).getKakaoRedirectUrl();
    }

    @Test
    void 카카오로그인_콜백_테스트() throws Exception {
        // Given
        String code = "valid_authorization_code";
        Long memberId = 1L;
        MemberLoginServiceResponse memberLoginServiceResponse = MemberLoginServiceResponse.from(new Member(
                1L,
                email,
                password,
                OauthProvider.KAKAO));
        when(memberService.kakaoLogin(code)).thenReturn(memberLoginServiceResponse);
        when(tokenService.createToken(memberId)).thenReturn(token);

        // When & Then
        mockMvc.perform(get("/api/members/login/kakao/callback")
                        .param("code", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.token").value(token));

        verify(memberService, times(1)).kakaoLogin(code);
        verify(tokenService, times(1)).createToken(memberId);
    }

    @Test
    void 포인트_추가_테스트() throws Exception {
        // Given
        int amount = 100;
        PointUpdateRequest request = new PointUpdateRequest(amount);
        MemberServiceResponse memberServiceResponse = new MemberServiceResponse(memberId, email, password);
        when(memberService.findById(anyLong())).thenReturn(memberServiceResponse);
        when(memberService.getPoint(memberId)).thenReturn(amount);

        // When & Then
        mockMvc.perform(patch("/api/members/point")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":" + amount + "}")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(amount));

        verify(memberService, times(1)).addPoint(eq(memberId), eq(amount));
    }

    @Test
    void 포인트_조회_테스트() throws Exception {
        // Given
        int point = 200;
        MemberServiceResponse memberServiceResponse = new MemberServiceResponse(memberId, email, password);
        when(memberService.findById(anyLong())).thenReturn(memberServiceResponse);
        when(memberService.getPoint(memberId)).thenReturn(point);

        // When & Then
        mockMvc.perform(get("/api/members/point")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(point));

        verify(memberService, times(1)).getPoint(memberId);
    }
}
