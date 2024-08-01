package gift.service;
import gift.entity.Category;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wishlist;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WishlistService wishlistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllWishlist() {
        // Given
        String token = "testToken";
        int page = 0;
        int size = 10;
        int memberId = 1;

        Category testCategory = new Category(1, "test", "test", "test", "test");
        Product testProduct1 = new Product(1, testCategory, 1, "test", "test");
        Product testProduct2 = new Product(2, testCategory, 1, "test", "test");
        Member testMember = new Member(1, "test", "test", "test");
        Wishlist testWishlist1 = new Wishlist(testMember, testProduct1, 1);
        Wishlist testWishlist2 = new Wishlist(testMember, testProduct2, 1);

        List<Wishlist> wishlists = Arrays.asList(testWishlist1, testWishlist2);

        when(memberRepository.searchIdByToken(token)).thenReturn(memberId);
        when(wishlistRepository.findByIdAndIdAndMember_id(anyInt(), anyInt(), eq(memberId))).thenReturn(wishlists);

        // When
        Page<Wishlist> result = wishlistService.getAllWishlist(token, page, size);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(memberRepository).searchIdByToken(token);
        verify(wishlistRepository).findByIdAndIdAndMember_id(anyInt(), anyInt(), eq(memberId));
    }

    @Test
    void testDeleteItem() {
        // Given
        String token = "testToken";
        int productId = 1;
        int memberId = 1;

        when(memberRepository.searchIdByToken(token)).thenReturn(memberId);
        when(wishlistRepository.searchCount_productByMember_idAndProduct_id(memberId, productId)).thenReturn(1);

        // When
        assertDoesNotThrow(() -> wishlistService.deleteItem(token, productId));

        // Then
        verify(memberRepository).searchIdByToken(token);
        verify(wishlistRepository).deleteByMember_idAndProduct_id(memberId, productId);
    }

    @Test
    void testDeleteItemNotFound() {
        // Given
        String token = "testToken";
        int productId = 1;
        int memberId = 1;

        when(memberRepository.searchIdByToken(token)).thenReturn(memberId);
        when(wishlistRepository.searchCount_productByMember_idAndProduct_id(memberId, productId)).thenReturn(0);

        // Then
        assertThrows(NoSuchElementException.class, () -> wishlistService.deleteItem(token, productId));
    }

    @Test
    void testChangeNum() {
        // Given
        String token = "testToken";
        int productId = 1;
        int count = 2;
        int memberId = 1;
        Category testCategory = new Category(1, "test", "test", "test", "test");
        Product product = new Product(2, testCategory, 1, "test", "test");
        Member member = new Member(1, "test", "test", "test");

        when(memberRepository.searchIdByToken(token)).thenReturn(memberId);
        when(memberRepository.findById(memberId)).thenReturn(member);
        when(productRepository.findById(productId)).thenReturn(product);

        // When
        assertDoesNotThrow(() -> wishlistService.changeNum(token, productId, count));

        // Then
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void testChangeNumDelete() {
        // Given
        String token = "testToken";
        int productId = 1;
        int count = 0;
        int memberId = 1;

        when(memberRepository.searchIdByToken(token)).thenReturn(memberId);

        // When
        assertDoesNotThrow(() -> wishlistService.changeNum(token, productId, count));

        // Then
        verify(wishlistRepository).deleteByMember_idAndProduct_id(memberId, productId);
    }

    @Test
    void testAddItem() {
        // Given
        String token = "testToken";
        int productId = 1;
        int memberId = 1;
        Category testCategory = new Category(1, "test", "test", "test", "test");
        Product product = new Product(1, testCategory, 1, "test", "test");
        Member member = new Member(1, "test", "test", "test");

        when(memberRepository.searchIdByToken(token)).thenReturn(memberId);
        when(memberRepository.findById(memberId)).thenReturn(member);
        when(productRepository.findById(productId)).thenReturn(product);
        when(wishlistRepository.searchCount_productByMember_idAndProduct_id(memberId, productId)).thenReturn(0);

        // When
        assertDoesNotThrow(() -> wishlistService.addItem(token, productId));

        // Then
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void testAddItemExisting() {
        // Given
        String token = "testToken";
        int productId = 1;
        int memberId = 1;
        Category testCategory = new Category(1, "test", "test", "test", "test");
        Product product = new Product(1, testCategory, 1, "test", "test");
        Member member = new Member(1, "test", "test", "test");

        when(memberRepository.searchIdByToken(token)).thenReturn(memberId);
        when(memberRepository.findById(memberId)).thenReturn(member);
        when(productRepository.findById(productId)).thenReturn(product);
        when(wishlistRepository.searchCount_productByMember_idAndProduct_id(memberId, productId)).thenReturn(1);

        // When
        assertDoesNotThrow(() -> wishlistService.addItem(token, productId));

        // Then
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void testIsItem() {
        // Given
        int memberId = 1;
        int productId = 1;

        when(wishlistRepository.searchCount_productByMember_idAndProduct_id(memberId, productId)).thenReturn(1);

        // When
        boolean result = wishlistService.isItem(memberId, productId);

        // Then
        assertTrue(result);
        verify(wishlistRepository).searchCount_productByMember_idAndProduct_id(memberId, productId);
    }
}
