package gift.wish;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.controller.WishController;
import gift.domain.Member.MemberRequest;
import gift.domain.Token;
import gift.domain.Wish.WishRequest;
import gift.domain.Wish.WishResponse;
import gift.service.MemberService;
import gift.service.WishService;
import gift.util.JwtUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WishController.class)
public class WishControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WishService wishService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private JwtUtil jwtUtil;

    private ObjectMapper objectMapper;
    private String token;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        MemberRequest member = new MemberRequest("admin2@kakao.com", "2222");
        Token mockToken = new Token("mocktoken");

        given(memberService.login(member)).willReturn(mockToken);
        token = mockToken.getToken();

        given(jwtUtil.isTokenValid(token)).willReturn(true);
        given(jwtUtil.extractMemberId(any())).willReturn(2L);

    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    @Test
    @DisplayName("위시 전체 조회 기능 테스트")
    void testGetWishListItems() throws Exception {
        //given
        Long memberId = 2L;
        List<WishResponse> responses = new ArrayList<>();

        WishResponse response1 = new WishResponse(1L,2L, 1L, "위시1", 1000L, "https://product1.jpg");
        WishResponse response2 = new WishResponse(2L,2L, 2L, "위시2", 2000L, "https://product2,jpg");

        responses.add(response1);
        responses.add(response2);

        Pageable pageable = PageRequest.of(0, 10); // 첫 번째 페이지, 페이지 크기 10

        // Page 객체 생성
        Page<WishResponse> page = new PageImpl<>(responses, pageable, responses.size());

        given(wishService.getWishListItems(memberId, 0,10)).willReturn(page);

        mockMvc.perform(get("/api/wish?page=0&size=10")
                .headers(getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        then(wishService).should(times(1)).getWishListItems(memberId, 0, 10);
    }

    @Test
    @DisplayName("위시 추가 테스트")
    void testAddWish() throws Exception {
        // given
        Long memberId = 2L;
        WishRequest request = new WishRequest(2L);

        // when
        mockMvc.perform(post("/api/wish")
                .headers(getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        then(wishService).should(times(1)).addWishListItem(memberId,request.productId());
    }

    @Test
    @DisplayName("위시 삭제 테스트")
    void testDeleteWish() throws Exception {
        // given
        Long wishId = 1L;
        // when
        mockMvc.perform(delete("/api/wish/{id}",wishId)
                .headers(getHttpHeaders()))
            .andExpect(status().isOk());

        then(wishService).should(times(1)).deleteWishListItem(wishId);
    }

}
