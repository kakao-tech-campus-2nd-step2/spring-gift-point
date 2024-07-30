package gift.domain.user.service;

import gift.external.api.kakao.KakaoApiProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@AutoConfigureMockMvc
@SpringBootTest
class KakaoLoginServiceTest {

    @Autowired
    private KakaoLoginService kakaoLoginService;

    @MockBean
    private KakaoApiProvider kakaoApiProvider;

    @MockBean
    private UserService userService;


//    @Test
//    @DisplayName("카카오 로그인 서비스 테스트 - 새로운 사용자")
//    void login_new_user() {
//        // given
//        JwtToken expected = new JwtToken("testToken");
//        UserResponse userResponse = new UserResponse(null, "testUser", "test@test.com", "test123", null);
//        KakaoToken kakaoToken = new KakaoToken(null, "testAccessToken", null, null, null);
//        KakaoUserInfo testUserInfo =
//            new KakaoUserInfo(
//                102345L, new KakaoAccount(
//                new Profile("testUser"),
//                true,
//                "test@test.com"));
//
//        given(kakaoApiProvider.getToken(eq("testCode"))).willReturn(kakaoToken);
//        doNothing().when(kakaoApiProvider).validateAccessToken(eq("testAccessToken"));
//        given(kakaoApiProvider.getUserInfo(eq("testAccessToken"))).willReturn(testUserInfo);
//
//        given(userService.readByEmail(eq("test@test.com"))).willReturn(userResponse);
//        given(userService.login(any(UserLoginRequest.class))).willReturn(expected);
//
//        // when
//        JwtToken actual = kakaoLoginService.login("testCode");
//
//        // then
//        assertThat(actual).isEqualTo(expected);
//    }
//
//    @Test
//    @DisplayName("카카오 로그인 서비스 테스트 - 기존 사용자")
//    void login_exist_user() {
//        // given
//        JwtToken expected = new JwtToken("testToken");
//        KakaoToken kakaoToken = new KakaoToken(null, "testAccessToken", null, null, null);
//        KakaoUserInfo testUserInfo =
//            new KakaoUserInfo(
//                102345L, new KakaoAccount(
//                new Profile("testUser"),
//                true,
//                "test@test.com"));
//
//        given(kakaoApiProvider.getToken(eq("testCode"))).willReturn(kakaoToken);
//        doNothing().when(kakaoApiProvider).validateAccessToken(eq("testAccessToken"));
//        given(kakaoApiProvider.getUserInfo(eq("testAccessToken"))).willReturn(testUserInfo);
//
//        given(userService.readByEmail(eq("test@test.com"))).willThrow(InvalidUserInfoException.class);
//        given(userService.signUp(any(UserRequest.class))).willReturn(expected);
//
//        // when
//        JwtToken actual = kakaoLoginService.login("testCode");
//
//        // then
//        assertThat(actual).isEqualTo(expected);
//    }
}