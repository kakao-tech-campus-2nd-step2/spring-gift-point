package gift.wishlist.application;

import gift.category.domain.Category;
import gift.exception.type.NotFoundException;
import gift.member.domain.Member;
import gift.member.domain.MemberRepository;
import gift.product.domain.Product;
import gift.product.domain.ProductRepository;
import gift.wishlist.domain.Wishlist;
import gift.wishlist.domain.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static gift.member.domain.OauthProvider.COMMON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private WishlistService wishlistService;

    private Long memberId;
    private Long productId;
    private Long wishlistId;
    private String memberEmail;
    private Member member;
    private Product product;
    private Wishlist wishlist;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        memberId = 1L;
        productId = 1L;
        wishlistId = 1L;
        memberEmail = "test@example.com";
        member = new Member(memberId, memberEmail, "password", COMMON);
        product = new Product(productId, "Product", 1000, "image", new Category("name", "color", "description", "imageUrl"));
        wishlist = new Wishlist(wishlistId, member, product);
    }

    @Test
    public void 위시리스트에_상품_추가() {
        // Given
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When
        wishlistService.save(memberId, productId);

        // Then
        verify(wishlistRepository, times(1)).save(any(Wishlist.class));
    }

    @Test
    public void 위시리스트에_존재하지_않는_사용자_추가_실패() {
        // Given
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> wishlistService.save(memberId, productId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("해당 사용자가 존재하지 않습니다.");
    }

    @Test
    public void 위시리스트에_존재하지_않는_상품_추가_실패() {
        // Given
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> wishlistService.save(memberId, productId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("해당 상품이 존재하지 않습니다.");
    }

    @Test
    public void 사용자의_위시리스트_페이징_조회() {
        // Given
        Wishlist wishlist2 = new Wishlist(2L, member, new Product(2L, "Product2", 2000, "image2", new Category("Category", "color", "description", "imageUrl")));
        Page<Wishlist> page = new PageImpl<>(List.of(wishlist, wishlist2), PageRequest.of(0, 2), 2);
        when(wishlistRepository.findAllByMemberId(eq(memberId), any(Pageable.class))).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 2);

        // When
        Page<WishlistServiceResponse> result = wishlistService.findAllByMemberId(memberId, pageable);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getMemberId()).isEqualTo(memberId);
    }

    @Test
    public void 특정_상품의_위시리스트_페이징_조회() {
        // Given
        Wishlist wishlist2 = new Wishlist(2L, member, product);
        Page<Wishlist> page = new PageImpl<>(List.of(wishlist, wishlist2), PageRequest.of(0, 2), 2);
        when(wishlistRepository.findAllByProductId(eq(productId), any(Pageable.class))).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 2);

        // When
        Page<WishlistServiceResponse> result = wishlistService.findAllByProductId(productId, pageable);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getProductId()).isEqualTo(productId);
    }

    @Test
    public void 위시리스트_삭제() {
        // Given
        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.of(wishlist));

        // When
        wishlistService.delete(wishlistId);

        // Then
        verify(wishlistRepository, times(1)).delete(wishlist);
    }

    @Test
    public void 존재하지_않는_위시리스트_삭제_실패() {
        // Given
        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> wishlistService.delete(wishlistId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("해당 위시리스트가 존재하지 않습니다.");
    }
}
