package gift.controller.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.configuration.FilterConfiguration;
import gift.domain.AuthToken;
import gift.domain.TokenInformation;
import gift.dto.request.MemberRequestDto;
import gift.dto.response.MemberResponseDto;
import gift.repository.token.TokenRepository;
import gift.service.AuthService;
import gift.service.KakaoService;
import gift.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static gift.utils.FilterConstant.NO_AUTHORIZATION_REDIRECT_URL;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthApiController.class)
@Import(FilterConfiguration.class)
class AuthApiControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    AuthService authService;

    @MockBean
    TokenService tokenService;

    @MockBean
    KakaoService kakaoService;

    @MockBean
    TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입, 로그인 시 MemberDto Invalid 로 인해 BadRequest Error 테스트")
    void 회원가입_로그인_DTO_INVALID_TEST() throws Exception{
        //given
        MemberRequestDto inValidMemberRequestDto = new MemberRequestDto("test", "p");

        //expected
        mvc.perform(post("/api/members/register")
                        .content(objectMapper.writeValueAsString(inValidMemberRequestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.email").value("이메일 형식을 맞춰 주세요"))
                .andDo(print());

    }
    @Test
    @DisplayName("회원가입 정상 성공 테스트")
    void 회원_가입_테스트() throws Exception {
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@pusan.ac.kr", "p");

        //expected
        mvc.perform(post("/api/members/register")
                        .content(objectMapper.writeValueAsString(memberRequestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 처음 로그인 정상 성공 테스트")
    void 회원_처음_로그인_테스트() throws Exception{
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@pusan.ac.kr", "password");
        MemberResponseDto memberResponseDto = new MemberResponseDto(1L, "test@pusan.ac.kr", "password", 0);

        given(authService.findOneByEmailAndPassword(memberRequestDto)).willReturn(memberResponseDto);

        //expected
        mvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(memberRequestDto))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    @DisplayName("카카오 로그인 성공 테스트")
    void 카카오_로그인_테스트() throws Exception{
        //given
        String code = "abcdefg";
        String kakaoAccessToken = "Test Token";
        String kakaoUserInformation = "3123";

        String response = "{" +
                "\"access_token\": \"Test Token\"," +
                "\"expires_in\": 30000," +
                "\"refresh_token\": \"Test Refresh Token\"," +
                "\"refresh_token_expires_in\": 70000" +
                "}";

        JsonNode jsonNode = objectMapper.readTree(response);

        TokenInformation tokenInfo = new TokenInformation(jsonNode);

        given(kakaoService.getKakaoOauthToken(code)).willReturn(tokenInfo);
        given(kakaoService.getKakaoUserInformation(kakaoAccessToken)).willReturn(kakaoUserInformation);
        given(authService.kakaoMemberLogin(kakaoUserInformation, tokenInfo)).willReturn("myToken");

        //expected
        mvc.perform(get("/api/members/login/oauth/kakao")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("code",code)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("myToken"))
                .andDo(print());
    }
}