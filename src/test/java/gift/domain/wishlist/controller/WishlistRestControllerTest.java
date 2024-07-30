package gift.domain.wishlist.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.user.entity.AuthProvider;
import gift.auth.jwt.JwtProvider;
import gift.domain.product.entity.Category;
import gift.domain.product.entity.Product;
import gift.domain.user.repository.UserJpaRepository;
import gift.domain.user.entity.Role;
import gift.domain.user.entity.User;
import gift.domain.wishlist.dto.WishItemRequestDto;
import gift.domain.wishlist.dto.WishItemResponseDto;
import gift.domain.wishlist.entity.WishItem;
import gift.domain.wishlist.service.WishlistService;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class WishlistRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishlistService wishlistService;

    @MockBean
    private UserJpaRepository userJpaRepository;

    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private ObjectMapper objectMapper;


    private static final User user = new User(1L, "testUser", "test@test.com", "test123", Role.USER, AuthProvider.LOCAL);
    private static final Category category = new Category(1L, "교환권", "#FFFFFF", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "test");
    private static final Product product = new Product(1L, category, "아이스 카페 아메리카노 T", 4500, "https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110563]_20210426095937947.jpg");

    private static final String DEFAULT_URL = "/api/wishlist";


    @BeforeEach
    void setUp() {
        given(userJpaRepository.findById(any(Long.class))).willReturn(Optional.of(user));

        Claims claims = Mockito.mock(Claims.class);
        given(jwtProvider.getAuthentication(any(String.class))).willReturn(claims);
        given(claims.getSubject()).willReturn(String.valueOf(user.getId()));
    }

    @Test
    @DisplayName("위시리스트 추가")
    void create_success() throws Exception {
        // given
        WishItemRequestDto wishItemRequestDto = new WishItemRequestDto(1L);
        String jsonContent = objectMapper.writeValueAsString(wishItemRequestDto);

        WishItem wishItem = wishItemRequestDto.toWishItem(user, product);
        given(wishlistService.create(any(WishItemRequestDto.class), any(User.class))).willReturn(WishItemResponseDto.from(wishItem));

        // when & then
        mockMvc.perform(post(DEFAULT_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent)
            .header("Authorization", "Bearer token"))
            .andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(WishItemResponseDto.from(wishItem))));
    }

    @Test
    @DisplayName("위시리스트 전체 조회")
    void readAll_success() throws Exception {
        // given
        List<WishItem> wishItems = List.of(new WishItem(1L, user, product));
        Page<WishItemResponseDto> expectedPage = new PageImpl<>(wishItems, PageRequest.of(0, 5),wishItems.size())
                                                    .map(WishItemResponseDto::from);

        given(wishlistService.readAll(any(Pageable.class), any(User.class))).willReturn(expectedPage);

        // when & then
        mockMvc.perform(get(DEFAULT_URL)
            .header("Authorization", "Bearer token"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedPage)));
    }


    @Test
    @DisplayName("위시리스트 삭제")
    void delete_success() throws Exception {
        // given
        willDoNothing().given(wishlistService).delete(1L);

        // when & then
        mockMvc.perform(delete(DEFAULT_URL + "/" + 1L)
            .header("Authorization", "Bearer token"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("위시리스트 사용자 ID로 삭제")
    void deleteAllByUserId_success() throws Exception {
        // given
        willDoNothing().given(wishlistService).deleteAllByUserId(user);

        // when & then
        mockMvc.perform(delete(DEFAULT_URL)
                .header("Authorization", "Bearer token"))
            .andExpect(status().isNoContent());
    }
}