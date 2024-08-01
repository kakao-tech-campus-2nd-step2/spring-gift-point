package gift.ControllerTest;

import gift.controller.KakaoController;
import gift.domain.Member.Member;
import gift.domain.WishList.WishList;
import gift.service.KakaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KakaoController.class)
public class KakaoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KakaoService kakaoService;

    @Test
    @DisplayName("code로 Member 정보 가져오기")
    void getUserInfomationTest() throws Exception {
        String code = "testCode";
        Member mockMember = new Member(null,"username", "password", new LinkedList<WishList>());
        given(kakaoService.getToken(code)).willReturn(mockMember);

        mockMvc.perform(get("/api/kakao/code")
                        .param("code", code)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("username"))
                .andExpect(jsonPath("$.password").value("password"));
    }
}
