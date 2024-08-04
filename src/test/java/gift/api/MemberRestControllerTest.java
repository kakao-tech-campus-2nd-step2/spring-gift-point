package gift.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.application.AuthService;
import gift.auth.dto.AuthResponse;
import gift.global.security.JwtFilter;
import gift.global.security.JwtUtil;
import gift.member.api.MemberRestController;
import gift.member.application.MemberService;
import gift.member.dto.MemberRequest;
import gift.member.dto.PointRequest;
import gift.member.entity.Member;
import gift.member.util.MemberMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import testFixtures.MemberFixture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberRestController.class)
@ExtendWith(MockitoExtension.class)
class MemberRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private JwtFilter jwtFilter;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private MemberService memberService;
    @MockBean
    private AuthService authService;

    private final String bearerToken = "Bearer token";

    @Test
    @DisplayName("회원가입 테스트")
    void signUp() throws Exception {
        MemberRequest request = new MemberRequest("test@email.com", "password");
        Member member = MemberMapper.toEntity(request);
        AuthResponse response = new AuthResponse("token");
        String requestJson = objectMapper.writeValueAsString(request);
        String responseJson = objectMapper.writeValueAsString(response);

        given(memberService.registerMember(any()))
                .willReturn(member);
        given(authService.generateAuthResponse(any()))
                .willReturn(response);

        mockMvc.perform(post("/api/members/register")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));

        verify(memberService).registerMember(any());
        verify(authService).generateAuthResponse(member);
    }

    @Test
    @DisplayName("로그인 테스트")
    void login() throws Exception {
        MemberRequest request = new MemberRequest("test@email.com", "password");
        AuthResponse response = new AuthResponse("token");
        String requestJson = objectMapper.writeValueAsString(request);
        String responseJson = objectMapper.writeValueAsString(response);

        given(authService.authenticate(request))
                .willReturn(response);

        mockMvc.perform(post("/api/members/login")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));

        verify(authService).authenticate(request);
    }

    @Test
    @DisplayName("회원 포인트 충전 테스트")
    void chargeMemberPoint() throws Exception {
        Long memberId = 1L;
        PointRequest request = new PointRequest(100);
        Member member = MemberFixture.createMember("test@email.com");
        String requestJson = objectMapper.writeValueAsString(request);

        given(memberService.getMemberByIdOrThrow(anyLong()))
                .willReturn(member);

        mockMvc.perform(put("/api/members/{memberId}/point", memberId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

}