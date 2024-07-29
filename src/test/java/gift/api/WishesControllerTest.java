package gift.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.product.entity.Category;
import gift.global.security.JwtFilter;
import gift.global.security.JwtUtil;
import gift.member.validator.LoginMember;
import gift.member.validator.LoginMemberArgumentResolver;
import gift.product.dto.ProductResponse;
import gift.product.util.ProductMapper;
import gift.wishlist.api.WishesController;
import gift.wishlist.application.WishesService;
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
import org.springframework.test.web.servlet.MockMvc;
import testFixtures.CategoryFixture;
import testFixtures.ProductFixture;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
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

    @BeforeEach
    void setUp() {
        memberId = 1L;
        category = CategoryFixture.createCategory("상품권");
    }

    @Test
    @DisplayName("위시 리스트 조회 기능 테스트")
    void getPagedWishes() throws Exception {
        List<ProductResponse> products = new ArrayList<>();
        ProductResponse productResponse1 = ProductMapper.toResponseDto(
                ProductFixture.createProduct("product1", category)
        );
        ProductResponse productResponse2 = ProductMapper.toResponseDto(
                ProductFixture.createProduct("product2", category)
        );
        products.add(productResponse1);
        products.add(productResponse2);
        Page<ProductResponse> response = new PageImpl<>(products);
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
        Long productId = 1L;

        given(loginMemberArgumentResolver.supportsParameter(argThat(parameter ->
                parameter.hasParameterAnnotation(LoginMember.class)))).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(memberId);

        mockMvc.perform(post("/api/wishes/{productId}", productId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk());

        verify(wishesService).addProductToWishlist(memberId, productId);
    }

    @Test
    @DisplayName("위시 리스트 상품 삭제 기능 테스트")
    void removeWish() throws Exception {
        Long productId = 1L;

        given(loginMemberArgumentResolver.supportsParameter(argThat(parameter ->
                parameter.hasParameterAnnotation(LoginMember.class)))).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(memberId);

        mockMvc.perform(delete("/api/wishes/{productId}", productId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk());

        verify(wishesService).removeWishIfPresent(memberId, productId);
    }

}