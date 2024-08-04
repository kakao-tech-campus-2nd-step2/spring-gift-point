package gift.service;

import gift.domain.*;
import gift.dto.request.WishlistRequest;
import gift.exception.MemberNotFoundException;
import gift.exception.WishlistNotFoundException;
import gift.repository.member.MemberSpringDataJpaRepository;
import gift.repository.product.ProductSpringDataJpaRepository;
import gift.repository.wishlist.WishlistSpringDataJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WishlistServiceTest {

    @Mock
    private WishlistSpringDataJpaRepository wishlistRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private ProductSpringDataJpaRepository productRepository;

    @Mock
    private MemberSpringDataJpaRepository memberRepository;

    @InjectMocks
    private WishlistService wishlistService;

    private static final Long MEMBER_ID = 1L;
    private static final Long PRODUCT_ID = 100L;
    private static final String VALID_TOKEN = "valid.token.test";

    private Member member;
    private Product product;

    @BeforeEach
    public void setup() {
        member = mock(Member.class);
        product = mock(Product.class);
    }

    @Test
    public void testAddItemToWishlist() {
        WishlistRequest request = new WishlistRequest(PRODUCT_ID);
        WishlistItem wishlistItem = new WishlistItem(member, product);

        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member));

        when(wishlistRepository.save(any(WishlistItem.class))).thenReturn(wishlistItem);

        wishlistService.addItemToWishlist(request, MEMBER_ID);

        verify(wishlistRepository, times(1)).save(any(WishlistItem.class));
    }

    @Test
    public void testDeleteItemFromWishlist() {
        WishlistItem wishlistItem = new WishlistItem(member, product);

        when(wishlistRepository.findByMemberIdAndProductId(MEMBER_ID, PRODUCT_ID)).thenReturn(Optional.of(wishlistItem));

        wishlistService.deleteItemFromWishlist(PRODUCT_ID, MEMBER_ID);

        verify(wishlistRepository, times(1)).delete(wishlistItem);
    }

    @Test
    public void testDeleteItemFromWishlistMemberNotFound() {
        assertThrows(WishlistNotFoundException.class, () -> wishlistService.deleteItemFromWishlist(PRODUCT_ID, MEMBER_ID));

        verify(wishlistRepository, never()).delete(any(WishlistItem.class));
    }

    @Test
    public void testGetWishlistByMemberId() {
        WishlistItem wishlistItem = new WishlistItem(member, product);
        List<WishlistItem> expectedItems = List.of(wishlistItem);

        when(wishlistRepository.findByMemberId(MEMBER_ID)).thenReturn(expectedItems);

        List<WishlistItem> actualItems = wishlistService.getWishlistByMemberId(MEMBER_ID);

        assertEquals(expectedItems.size(), actualItems.size());
        assertEquals(expectedItems.getFirst().getProduct().getId(), actualItems.getFirst().getProduct().getId());
    }
}
