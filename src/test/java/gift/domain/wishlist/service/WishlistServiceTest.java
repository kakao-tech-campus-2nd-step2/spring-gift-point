package gift.domain.wishlist.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import gift.domain.member.entity.AuthProvider;
import gift.domain.product.repository.ProductJpaRepository;
import gift.domain.product.entity.Category;
import gift.domain.product.entity.Product;
import gift.domain.member.entity.Role;
import gift.domain.member.entity.Member;
import gift.domain.wishlist.dto.WishItemResponseDto;
import gift.domain.wishlist.repository.WishlistJpaRepository;
import gift.domain.wishlist.dto.WishItemRequestDto;
import gift.domain.wishlist.entity.WishItem;
import gift.exception.InvalidProductInfoException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@AutoConfigureMockMvc
@SpringBootTest
class WishlistServiceTest {

    @Autowired
    private WishlistService wishlistService;

    @MockBean
    private WishlistJpaRepository wishlistJpaRepository;

    @MockBean
    private ProductJpaRepository productJpaRepository;


    private static final Member MEMBER = new Member(1L, "testUser", "test@test.com", "test123", Role.USER, AuthProvider.LOCAL);
    private static final Category category = new Category(1L, "교환권", "#FFFFFF", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "test");
    private static final Product product = new Product(1L, category, "아이스 카페 아메리카노 T", 4500, "https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110563]_20210426095937947.jpg");


    @Test
    @DisplayName("위시리스트 추가 성공")
    void create_success() {
        // given
        WishItemRequestDto wishItemRequestDto = new WishItemRequestDto(1L);
        given(productJpaRepository.findById(anyLong())).willReturn(Optional.of(product));

        WishItem wishItem = wishItemRequestDto.toWishItem(MEMBER, product);
        given(wishlistJpaRepository.save(any(WishItem.class))).willReturn(wishItem);

        // when
        WishItemResponseDto savedWishItem = wishlistService.create(wishItemRequestDto, MEMBER);

        // then
        assertAll(
            () -> assertThat(savedWishItem).isNotNull(),
            () -> assertThat(savedWishItem.memberId()).isEqualTo(wishItem.getMemberId()),
            () -> assertThat(savedWishItem.productId()).isEqualTo(wishItem.getMemberId())
        );
    }

    @Test
    @DisplayName("위시리스트 추가 서비스 실패 - 존재하지 않는 상품 ID")
    void create_fail_product_id_error() {
        // given
        WishItemRequestDto wishItemRequestDto = new WishItemRequestDto(2L);
        given(productJpaRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> wishlistService.create(wishItemRequestDto, MEMBER))
            .isInstanceOf(InvalidProductInfoException.class)
            .hasMessage("error.invalid.product.id");
    }

    @Test
    @DisplayName("위시리스트 조회 성공")
    void readAll_success() {
        // given
        Product product2 = new Product(2L, category, "아이스 카페 라떼 T", 4500, "https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110563]_20210426095937947.jpg");

        List<WishItem> wishItemList = List.of(
            new WishItem(1L, MEMBER, product),
            new WishItem(2L, MEMBER, product2)
        );

        given(wishlistJpaRepository.findAllByMemberId(eq(MEMBER.getId()), any(Pageable.class)))
            .willReturn(new PageImpl<>(wishItemList));

        // when
        Page<WishItemResponseDto> wishItems = wishlistService.readAll(PageRequest.of(0, 5), MEMBER);

        // then
        assertAll(
            () -> assertThat(wishItems.getSize()).isEqualTo(2),
            () -> assertThat(wishItems.getTotalElements()).isEqualTo(2),
            () -> assertThat(wishItems.getTotalPages()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("위시리스트 삭제 성공")
    void delete_success() {
        // given
        WishItem wishItem = new WishItem(1L, MEMBER, product);
        given(wishlistJpaRepository.findById(anyLong())).willReturn(Optional.of(wishItem));
        willDoNothing().given(wishlistJpaRepository).delete(any(WishItem.class));

        // when
        wishlistService.delete(1L);

        // then
        Page<WishItemResponseDto> wishlist = wishlistService.readAll(PageRequest.of(0, 5), MEMBER);
        assertThat(wishlist).isEmpty();
    }

    @Test
    @DisplayName("위시리스트 삭제 실패 - 존재하지 않는 항목 ID")
    void delete_fail_id_error() {
        // given
        given(wishlistJpaRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> wishlistService.delete(2L))
            .isInstanceOf(InvalidProductInfoException.class)
            .hasMessage("error.invalid.product.id");
    }

    @Test
    @DisplayName("위시리스트 사용자 ID로 삭제 성공")
    void deleteAllByMemberId_success() {
        // given
        given(wishlistJpaRepository.findAllByMemberId(eq(MEMBER.getId()), any(Pageable.class)))
            .willReturn(new PageImpl<>(Collections.EMPTY_LIST));
        willDoNothing().given(wishlistJpaRepository).deleteAllByMemberId(anyLong());

        // when
        wishlistService.deleteAllByMemberId(MEMBER);

        // then
        Page<WishItemResponseDto> wishlist = wishlistService.readAll(PageRequest.of(0, 5), MEMBER);
        assertThat(wishlist).isEmpty();
    }
}