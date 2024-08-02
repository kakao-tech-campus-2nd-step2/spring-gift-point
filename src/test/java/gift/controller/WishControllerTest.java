package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.*;
import gift.dto.request.WishRequestDto;
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
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static gift.utils.FilterConstant.NO_AUTHORIZATION_REDIRECT_URL;
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

        mockMvc.perform(get("/api/wishes"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.message").value("인증되지 않은 사용자 입니다."))
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

        Category category = new Category("TEST_CATEGORY", "#0000", "abc.png");

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

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<WishResponseDto> wishResponseDtos = wishes.subList(15, 20).reversed().stream()
                .map(WishResponseDto::from)
                .collect(Collectors.toList());

        Page<WishResponseDto> wishResponseDtoPage = new PageImpl<>(wishResponseDtos, pageRequest, wishResponseDtos.size());

        AuthToken authToken = new AuthToken("테스트용 인증코드", "abc@pusan.ac.kr");

        given(tokenService.findToken(authToken.getToken())).willReturn(authToken);
        given(wishService.findWishesPaging(authToken.getEmail(), pageRequest)).willReturn(wishResponseDtoPage);

        //expected
        mvc.perform(get("/api/wishes")
                        .header("Authorization","Bearer 테스트용 인증코드")
                        .param("page","0")
                        .param("size","5")
                        .param("sort","createdDate,desc")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
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
        WishRequestDto inValidWishRequestDto = new WishRequestDto(null, -1);


        //expected
        mvc.perform(post("/api/wishes")
                        .header("Authorization","Bearer 테스트용 인증코드")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inValidWishRequestDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.productId").value("WISH LIST에 추가하고 싶은 상품을 선택해 주세요"))
                .andExpect(jsonPath("$.validation.count").value("개수은 1개 이상이어야 합니다"))
                .andDo(print());
    }
    @Test
    @DisplayName("위시 정상 저장 테스트")
    void 위시_저장_테스트() throws Exception {
        //given
        AuthToken authToken = new AuthToken("테스트용 인증코드", "abc@pusan.ac.kr");

        WishRequestDto wishRequestDto = new WishRequestDto(1L, 20);
        WishResponseDto wishResponseDto = new WishResponseDto(1L, 1L, 20);

        given(tokenService.findToken(authToken.getToken())).willReturn(authToken);
        given(wishService.addWish(1L, authToken.getEmail(), 20)).willReturn(wishResponseDto);


        //expected
        mvc.perform(post("/api/wishes")
                        .header("Authorization","Bearer 테스트용 인증코드")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wishRequestDto))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.productId").value("1"))
                .andExpect(jsonPath("$.count").value("20"))
                .andDo(print());

        verify(wishService, times(1)).addWish(anyLong(), anyString(), anyInt());
    }


    @Test
    @DisplayName("위시 정상 삭제 테스트")
    void 위시_정상_삭제_테스트() throws Exception {
        //given
        AuthToken authToken = new AuthToken("테스트용 인증코드", "abc@pusan.ac.kr");
        WishResponseDto wishResponseDto = new WishResponseDto(1L, 1L, 3);

        given(tokenService.findToken(authToken.getToken())).willReturn(authToken);
        given(wishService.deleteWish(1L, authToken.getEmail())).willReturn(wishResponseDto);

        //expected
        mvc.perform(delete("/api/wishes/{wishId}","1")
                        .header("Authorization","Bearer 테스트용 인증코드")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.productId").value("1"))
                .andExpect(jsonPath("$.count").value("3"))
                .andDo(print());
    }

}