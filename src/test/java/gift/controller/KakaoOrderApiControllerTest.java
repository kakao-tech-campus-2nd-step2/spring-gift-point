package gift.controller;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.error.KakaoOrderException;
import gift.users.kakao.KakaoOrderApiController;
import gift.users.kakao.KakaoOrderDTO;
import gift.users.kakao.KakaoOrderService;
import gift.users.kakao.KakaoProperties;
import gift.users.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KakaoOrderApiControllerTest {

    private final KakaoOrderService kakaoOrderService = mock(KakaoOrderService.class);
    private MockMvc mvc;
    private KakaoOrderApiController kakaoOrderApiController;
    private UserService userService = mock(UserService.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private KakaoProperties kakaoProperties;

    @BeforeEach
    void beforeEach(){
        kakaoOrderApiController = new KakaoOrderApiController(kakaoOrderService, userService);
        mvc = MockMvcBuilders.standaloneSetup(kakaoOrderApiController)
            .defaultResponseCharacterEncoding(UTF_8)
            .build();
    }

    @Test
    @DisplayName("카카오 주문하기")
    void kakaoOrder() throws Exception {
        //given
        given(userService.findSns(anyLong())).willReturn("kakao");
        KakaoOrderDTO kakaoOrderDTO = new KakaoOrderDTO(1L,
            1L, 2, "1", "hello");
        KakaoOrderDTO kakaoOrderResponse = new KakaoOrderDTO(1L,
            1L, 2, "2024-10-11", "hello");
        given(kakaoOrderService.kakaoOrder(anyLong(), any(KakaoOrderDTO.class), anyString()))
            .willReturn(kakaoOrderResponse);

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.post("/api/orders/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(kakaoOrderDTO)));

        //then
        resultActions.andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(kakaoOrderResponse)));
    }

    @Test
    @DisplayName("카카오 주문하기 실패 카카오 회원만 이용 가능")
    void kakaoOrderFailedOnlyKakaoUsersAllowed() {
        //given
        given(userService.findSns(anyLong())).willReturn("local");
        KakaoOrderDTO kakaoOrderDTO = new KakaoOrderDTO(1L,
            1L, 2, "1", "hello");

        //when

        //then
        assertThatThrownBy(() -> kakaoOrderApiController.kakaoOrder(1L, kakaoOrderDTO))
            .isInstanceOf(KakaoOrderException.class)
            .hasMessageContaining("카카오 유저만 이용할 수 있는 서비스입니다.");
    }
}
