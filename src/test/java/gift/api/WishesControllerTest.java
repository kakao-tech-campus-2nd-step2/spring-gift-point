package gift.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.global.security.JwtFilter;
import gift.global.security.JwtUtil;
import gift.member.validator.LoginMember;
import gift.member.validator.LoginMemberArgumentResolver;
import gift.product.entity.Category;
import gift.product.entity.Option;
import gift.product.entity.Product;
import gift.product.util.OptionMapper;
import gift.wishlist.api.WishesController;
import gift.wishlist.application.WishesService;
import gift.wishlist.dto.WishResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import testFixtures.CategoryFixture;
import testFixtures.OptionFixture;
import testFixtures.ProductFixture;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WishesController.class)
@ExtendWith(MockitoExtension.class)
class WishesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private JwtFilter jwtFilter;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;
    @MockBean
    private WishesService wishesService;

    private final static String bearerToken = "Bearer token";
    private Long memberId;
    private Category category;
    private Product product;
    private Option option;

    @BeforeEach
    void setUp() {
        memberId = 1L;
        category = CategoryFixture.createCategory("상품권");
        product = ProductFixture.createProduct("product", category);
        option = OptionFixture.createOption("옵션", product);
    }

    @Test
    @DisplayName("위시 리스트 조회 기능 테스트")
    void getPagedWishes() throws Exception {
        List<WishResponse> wishes = new ArrayList<>();
        WishResponse wishResponse1 = new WishResponse(1L, OptionMapper.toWishResponseDto(option));
        WishResponse wishResponse2 = new WishResponse(2L, OptionMapper.toWishResponseDto(option));
        
        wishes.add(wishResponse1);
        wishes.add(wishResponse2);
        Page<WishResponse> response = new PageImpl<>(wishes);
        String responseJson = objectMapper.writeValueAsString(response);

        given(wishesService.getWishlistOfMember(anyLong(), any()))
                .willReturn(response);
        given(loginMemberArgumentResolver.supportsParameter(argThat(parameter ->
                parameter.hasParameterAnnotation(LoginMember.class)))).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(memberId);

        mockMvc.perform(get("/api/wishes")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));

        verify(wishesService).getWishlistOfMember(eq(memberId), any(Pageable.class));
    }

    @Test
    @DisplayName("위시 리스트 상품 추가 기능 테스트")
    void addWish() throws Exception {
        Long optionId = 1L;
        String requestJson = objectMapper.writeValueAsString(optionId);

        given(loginMemberArgumentResolver.supportsParameter(argThat(parameter ->
                parameter.hasParameterAnnotation(LoginMember.class)))).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(memberId);

        mockMvc.perform(post("/api/wishes")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(wishesService).addProductToWishlist(memberId, optionId);
    }

    @Test
    @DisplayName("위시 리스트 상품 삭제 기능 테스트")
    void removeWish() throws Exception {
        Long optionId = 1L;

        given(loginMemberArgumentResolver.supportsParameter(argThat(parameter ->
                parameter.hasParameterAnnotation(LoginMember.class)))).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(memberId);

        mockMvc.perform(delete("/api/wishes/{optionId}", optionId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk());

        verify(wishesService).removeWishById(optionId);
    }

}