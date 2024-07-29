package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.*;
import gift.dto.request.WishCreateRequest;
import gift.dto.request.WishDeleteRequest;
import gift.dto.request.WishEditRequest;
import gift.dto.response.WishResponseDto;
import gift.filter.AuthFilter;
import gift.filter.LoginFilter;
import gift.filter.MyTokenFilter;
import gift.filter.OAuthTokenFilter;
import gift.repository.token.TokenRepository;
import gift.service.TokenService;
import gift.service.WishService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WishController.class)
class WishControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    MockMvc mvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private WishService wishService;

    @MockBean
    TokenRepository tokenRepository;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("필터 통과 실패 테스트")
    void 필터_통과_실패_테스트() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(new AuthFilter(tokenRepository))
                .addFilter(new MyTokenFilter(tokenRepository))
                .addFilter(new OAuthTokenFilter(tokenRepository))
                .addFilter(new LoginFilter(tokenRepository))
                .build();

        mockMvc.perform(get("/wishes"))
                .andExpect(redirectedUrl("/home"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @Test
    @DisplayName("위시 정상 조회 페이징 API 테스트")
    void 위시_전체_정상_조회_API_TEST() throws Exception {
        //given
        Member member = new Member.Builder()
                .email("abc@pusan.ac.kr")
                .password("abc")
                .build();

        Category category = new Category("TEST_CATEGORY", "#0000");

        List<Wish> wishes = new ArrayList<>();

        for(int i=0; i<20; i++){
            Product product = new Product.Builder()
                    .name("테스트" + i)
                    .price(i)
                    .imageUrl("abc.png")
                    .category(category)
                    .build();

            Wish wish = new Wish.Builder()
                    .member(member)
                    .product(product)
                    .count(i)
                    .build();

            wishes.add(wish);
        }

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<WishResponseDto> wishResponseDtos = wishes.subList(15, 20).reversed().stream()
                .map(WishResponseDto::from)
                .collect(Collectors.toList());

        AuthToken authToken = new AuthToken("테스트용 인증코드", "abc@pusan.ac.kr");

        given(tokenService.findToken(authToken.getToken())).willReturn(authToken);
        given(wishService.findWishesPaging(authToken.getEmail(), pageRequest)).willReturn(wishResponseDtos);

        //expected
        mvc.perform(get("/wishes")
                        .header("Authorization","Bearer 테스트용 인증코드")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andDo(print());


        BDDMockito.verify(tokenService, times(1)).findToken(any(String.class));
        BDDMockito.verify(wishService, times(1)).findWishesPaging(any(String.class), any(Pageable.class));
    }

    @Test
    @DisplayName("위시 저장 DTO INVALID 테스트")
    void 위시_저장_DTO_INVALID_테스트() throws Exception{
        //given
        AuthToken authToken = new AuthToken("테스트용 인증코드", "abc@pusan.ac.kr");
        given(tokenService.findToken(authToken.getToken())).willReturn(authToken);
        WishCreateRequest inValidWishCreateRequest = new WishCreateRequest(null, -1);


        //expected
        mvc.perform(post("/wishes")
                        .header("Authorization","Bearer 테스트용 인증코드")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inValidWishCreateRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.product_id").value("WISH LIST에 추가하고 싶은 상품을 선택해 주세요"))
                .andExpect(jsonPath("$.validation.count").value("개수은 1개 이상이어야 합니다"))
                .andDo(print());
    }
    @Test
    @DisplayName("위시 정상 저장 테스트")
    void 위시_저장_테스트() throws Exception {
        //given
        AuthToken authToken = new AuthToken("테스트용 인증코드", "abc@pusan.ac.kr");

        given(tokenService.findToken(authToken.getToken())).willReturn(authToken);

        WishCreateRequest wishCreateRequest = new WishCreateRequest(1L, 20);

        //expected
        mvc.perform(post("/wishes")
                        .header("Authorization","Bearer 테스트용 인증코드")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wishCreateRequest))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/wishes"))
                .andDo(print());

        verify(wishService, times(1)).addWish(anyLong(), anyString(), anyInt());
    }

    @Test
    @DisplayName("위시 수정 DTO INVALID 테스트")
    void 위시_수정_DTO_INVALID_테스트() throws Exception{
        //given
        AuthToken authToken = new AuthToken("테스트용 인증코드", "abc@pusan.ac.kr");

        given(tokenService.findToken(authToken.getToken())).willReturn(authToken);

        WishEditRequest inValidWishEditRequest = new WishEditRequest(null, -1);


        //expected
        mvc.perform(put("/wishes")
                        .header("Authorization","Bearer 테스트용 인증코드")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inValidWishEditRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.wish_id").value("WISH LIST에서 수정하고 싶은 상품을 선택해 주세요"))
                .andExpect(jsonPath("$.validation.count").value("개수은 1개 이상이어야 합니다"))
                .andDo(print());
    }

    @Test
    @DisplayName("위시 정상 수정 테스트")
    void 위시_정상_수정_테스트() throws Exception {
        //given
        AuthToken authToken = new AuthToken("테스트용 인증코드", "abc@pusan.ac.kr");

        given(tokenService.findToken(authToken.getToken())).willReturn(authToken);

        WishEditRequest wishEditRequest = new WishEditRequest(1L, 20);

        //expected
        mvc.perform(put("/wishes")
                        .header("Authorization","Bearer 테스트용 인증코드")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wishEditRequest))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/wishes"));

        verify(wishService, times(1)).editWish(anyLong(), anyString(), anyInt());
    }

    @Test
    @DisplayName("위시 삭제 시 DTO INVALID 테스트")
    void 위시_삭제_DTO_INVALID_테스트() throws Exception {
        //given
        AuthToken authToken = new AuthToken("테스트용 인증코드", "abc@pusan.ac.kr");

        given(tokenService.findToken(authToken.getToken())).willReturn(authToken);

        WishDeleteRequest inValidWishDeleteRequest = new WishDeleteRequest(null);


        //expected
        mvc.perform(delete("/wishes")
                        .header("Authorization","Bearer 테스트용 인증코드")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inValidWishDeleteRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.wish_id").value("WISH LIST에 삭제하고 싶은 상품을 선택해 주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("위시 정상 삭제 테스트")
    void 위시_정상_삭제_테스트() throws Exception {
        //given
        AuthToken authToken = new AuthToken("테스트용 인증코드", "abc@pusan.ac.kr");

        given(tokenService.findToken(authToken.getToken())).willReturn(authToken);

        WishDeleteRequest wishDeleteRequest = new WishDeleteRequest(1L);

        //expected
        mvc.perform(delete("/wishes")
                        .header("Authorization","Bearer 테스트용 인증코드")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wishDeleteRequest))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/wishes"))
                .andDo(print());

        verify(wishService, times(1)).deleteWish(anyLong(), anyString());
    }

}