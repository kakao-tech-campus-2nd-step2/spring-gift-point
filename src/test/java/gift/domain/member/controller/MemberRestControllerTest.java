package gift.domain.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.jwt.JwtProvider;
import gift.auth.jwt.JwtToken;
import gift.domain.member.dto.MemberLoginRequest;
import gift.domain.member.dto.MemberLoginResponse;
import gift.domain.member.dto.MemberRequest;
import gift.domain.member.dto.PointRechargeRequest;
import gift.domain.member.dto.PointResponse;
import gift.domain.member.entity.AuthProvider;
import gift.domain.member.entity.Member;
import gift.domain.member.entity.Role;
import gift.domain.member.repository.MemberJpaRepository;
import gift.domain.member.service.MemberService;
import gift.exception.InvalidUserInfoException;
import io.jsonwebtoken.Claims;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

    @MockBean
    private MemberJpaRepository memberJpaRepository;

    @MockBean
    private JwtProvider jwtProvider;

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
        MemberRequest memberRequest = new MemberRequest("test@test.com", "test123");
        String jsonContent = objectMapper.writeValueAsString(memberRequest);

        JwtToken expectedJwtToken = new JwtToken("token");
        MemberLoginResponse expected = new MemberLoginResponse(memberRequest.email(), expectedJwtToken.token());

        given(memberService.signUp(any(MemberRequest.class))).willReturn(expectedJwtToken);

        // when & then
        mockMvc.perform(postRequest(REGISTER_URL, jsonContent))
            .andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(expected)))
            .andDo(print());
    }

    @Test
    @DisplayName("회원 가입에 실패하는 경우 - 이미 존재하는 이메일로 가입 시도")
    void create_fail() throws Exception {
        // given
        MemberRequest memberRequest = new MemberRequest("test@test.com", "test123");
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
        MemberLoginResponse expected = new MemberLoginResponse(memberLoginRequest.email(), expectedJwtToken.token());

        given(memberService.login(any(MemberLoginRequest.class))).willReturn(expectedJwtToken);

        // when & then
        mockMvc.perform(postRequest(LOGIN_URL, jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expected)))
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

    @Test
    @DisplayName("포인트 충전")
    void recharge_success() throws Exception {
        // given
        Member member = new Member(1L, "testUser", "test@test.com", "test123", Role.USER, AuthProvider.LOCAL);
        member.rechargePoint(1000);

        given(memberJpaRepository.findById(any(Long.class))).willReturn(Optional.of(member));

        Claims claims = Mockito.mock(Claims.class);
        given(jwtProvider.getAuthentication(any(String.class))).willReturn(claims);
        given(claims.getSubject()).willReturn(String.valueOf(member.getId()));

        PointRechargeRequest pointRechargeRequest = new PointRechargeRequest(10000);
        PointResponse pointResponse = new PointResponse(11000);

        given(memberService.rechargePoint(any(PointRechargeRequest.class), eq(member))).willReturn(pointResponse);

        // when & then
        mockMvc.perform(patch("/api/members/point")
            .content(objectMapper.writeValueAsString(pointRechargeRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer token"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(pointResponse)));
    }

    @Test
    @DisplayName("포인트 조회")
    void read_point_success() throws Exception {
        // given
        Member member = new Member(1L, "testUser", "test@test.com", "test123", Role.USER, AuthProvider.LOCAL);
        member.rechargePoint(1000);

        given(memberJpaRepository.findById(any(Long.class))).willReturn(Optional.of(member));

        Claims claims = Mockito.mock(Claims.class);
        given(jwtProvider.getAuthentication(any(String.class))).willReturn(claims);
        given(claims.getSubject()).willReturn(String.valueOf(member.getId()));

        PointResponse pointResponse = new PointResponse(1000);

        given(memberService.readPoint(eq(member))).willReturn(pointResponse);

        // when & then
        mockMvc.perform(get("/api/members/point")
            .header("Authorization", "Bearer token"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(pointResponse)));
    }
}