package gift.application;

import gift.product.dao.OptionRepository;
import gift.product.entity.Category;
import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.member.dao.MemberRepository;
import gift.member.entity.Member;
import gift.product.entity.Option;
import gift.product.entity.Product;
import gift.wishlist.application.WishesService;
import gift.wishlist.dao.WishesRepository;
import gift.wishlist.dto.WishResponse;
import gift.wishlist.entity.Wish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import testFixtures.CategoryFixture;
import testFixtures.MemberFixture;
import testFixtures.ProductFixture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WishesServiceTest {

    @InjectMocks
    private WishesService wishesService;

    @Mock
    private WishesRepository wishesRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private MemberRepository memberRepository;

    private Long memberId;
    private Long optionId;
    private Member member;
    private Option option;

    @BeforeEach
    void setUp() {
        memberId = 1L;
        member = MemberFixture.createMember("test@email.com");
        optionId = 2L;
        Category category = CategoryFixture.createCategory("상품권");
        Product product = ProductFixture.createProduct("product", category);
        option = new Option("옵션", 1, product);
    }

    @Test
    @DisplayName("위시 리스트 상품 추가 서비스 테스트")
    void addProductToWishlist() {
        given(wishesRepository.findByMember_IdAndOption_Id(anyLong(), anyLong()))
                .willReturn(Optional.empty());
        given(memberRepository.findById(any()))
                .willReturn(Optional.of(member));
        given(optionRepository.findById(any()))
                .willReturn(Optional.of(option));

        wishesService.addProductToWishlist(memberId, optionId);

        verify(wishesRepository).findByMember_IdAndOption_Id(memberId, optionId);
        verify(memberRepository).findById(memberId);
        verify(optionRepository).findById(optionId);
        verify(wishesRepository).save(any());
    }

    @Test
    @DisplayName("위시 리스트 중복 상품 추가 테스트")
    void addDuplicatedProductToWishlist() {
        Wish wish = new Wish(member, option);
        given(wishesRepository.findByMember_IdAndOption_Id(anyLong(), anyLong()))
                .willReturn(Optional.of(wish));

        assertThatThrownBy(() -> wishesService.addProductToWishlist(memberId, optionId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.WISH_ALREADY_EXISTS
                                     .getMessage());
    }

    @Test
    @DisplayName("위시 리스트 상품 삭제 서비스 테스트")
    void removeProductIfPresent() {
        Wish wish = new Wish(member, option);
        given(wishesRepository.findByMember_IdAndOption_Id(anyLong(), anyLong()))
                .willReturn(Optional.of(wish));

        wishesService.removeWishIfPresent(memberId, optionId);

        verify(wishesRepository).findByMember_IdAndOption_Id(memberId, optionId);
        verify(wishesRepository).deleteById(any());
    }

    @Test
    @DisplayName("위시 리스트 조회 서비스 테스트")
    void getWishlistOfMember() {
        List<Wish> wishes = new ArrayList<>();
        Wish wish = new Wish(member, option);
        wishes.add(wish);
        wishes.add(wish);
        Page<Wish> wishlist = new PageImpl<>(wishes);
        given(wishesRepository.findByMember_Id(anyLong(), any()))
                .willReturn(wishlist);

        Page<WishResponse> foundWishlist = wishesService.getWishlistOfMember(
                memberId,
                PageRequest.of(0, 10)
        );

        assertThat(foundWishlist.getTotalElements()).isEqualTo(wishes.size());
    }

}