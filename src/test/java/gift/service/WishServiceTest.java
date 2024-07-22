package gift.service;

import static gift.util.constants.ProductConstants.PRODUCT_NOT_FOUND;
import static gift.util.constants.WishConstants.ALREADY_EXISTS;
import static gift.util.constants.WishConstants.PERMISSION_DENIED;
import static gift.util.constants.WishConstants.WISH_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gift.dto.member.MemberResponse;
import gift.dto.product.ProductResponse;
import gift.dto.wish.WishCreateRequest;
import gift.dto.wish.WishResponse;
import gift.exception.product.ProductNotFoundException;
import gift.exception.wish.DuplicateWishException;
import gift.exception.wish.PermissionDeniedException;
import gift.exception.wish.WishNotFoundException;
import gift.model.Category;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import gift.repository.WishRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class WishServiceTest {

    @Mock
    private WishRepository wishRepository;

    @Mock
    private ProductService productService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private WishService wishService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("위시리스트에 상품 추가 성공 테스트")
    public void testAddWishSuccess() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Member member = new Member(1L, "test@example.com", "password");
        MemberResponse memberResponse = new MemberResponse(1L, "test@example.com", null);
        ProductResponse productResponse = new ProductResponse(
            1L,
            "Product",
            100,
            "imageUrl",
            category.getId(),
            "categoryName"
        );
        Product product = new Product(
            productResponse.id(),
            productResponse.name(),
            productResponse.price(),
            productResponse.imageUrl(),
            category
        );

        when(memberService.getMemberById(1L)).thenReturn(memberResponse);
        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(memberService.convertToEntity(any(MemberResponse.class))).thenReturn(member);
        when(productService.convertToEntity(any(ProductResponse.class))).thenReturn(product);
        when(wishRepository.existsByMember_IdAndProduct_Id(1L, 1L)).thenReturn(false);
        when(wishRepository.save(any(Wish.class))).thenReturn(new Wish(1L, member, product));

        WishCreateRequest wishCreateRequest = new WishCreateRequest(1L);
        WishResponse response = wishService.addWish(wishCreateRequest, 1L);

        assertEquals(1L, response.memberId());
        assertEquals(1L, response.productId());
        assertNotNull(response.id());
    }

    @Test
    @DisplayName("위시리스트에 없는 상품 ID 추가 시도")
    public void testAddWishProductNotFound() {
        WishCreateRequest wishCreateRequest = new WishCreateRequest(999L);

        when(productService.getProductById(999L)).thenThrow(
            new ProductNotFoundException(PRODUCT_NOT_FOUND + 999));

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            wishService.addWish(wishCreateRequest, 1L);
        });

        assertEquals(PRODUCT_NOT_FOUND + "999", exception.getMessage());
    }

    @Test
    @DisplayName("위시리스트에 이미 존재하는 상품 추가 시도")
    public void testAddWishDuplicate() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Member member = new Member(1L, "test@example.com", "password");
        MemberResponse memberResponse = new MemberResponse(1L, "test@example.com", null);
        Product product = new Product(1L, "Product", 100, "imageUrl", category);
        ProductResponse productResponse = new ProductResponse(
            1L,
            "Product",
            100,
            "imageUrl",
            category.getId(),
            "categoryName"
        );

        when(memberService.getMemberById(1L)).thenReturn(memberResponse);
        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(memberService.convertToEntity(any(MemberResponse.class))).thenReturn(member);
        when(productService.convertToEntity(any(ProductResponse.class))).thenReturn(product);
        when(wishRepository.existsByMember_IdAndProduct_Id(1L, 1L)).thenReturn(true);

        WishCreateRequest wishCreateRequest = new WishCreateRequest(1L);
        DuplicateWishException exception = assertThrows(DuplicateWishException.class, () -> {
            wishService.addWish(wishCreateRequest, 1L);
        });

        assertEquals(ALREADY_EXISTS, exception.getMessage());
    }

    @Test
    @DisplayName("위시리스트에서 상품 삭제 성공 테스트")
    public void testDeleteWishSuccess() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Member member = new Member(1L, "test@example.com", "password");
        Product product = new Product(1L, "Product", 100, "imageUrl", category);
        Wish wish = new Wish(1L, member, product);

        when(wishRepository.findById(1L)).thenReturn(Optional.of(wish));

        wishService.deleteWish(1L, 1L);
    }

    @Test
    @DisplayName("위시리스트에서 없는 상품 ID로 삭제 시도")
    public void testDeleteWishNotFound() {
        when(wishRepository.findById(999L)).thenReturn(Optional.empty());

        WishNotFoundException exception = assertThrows(WishNotFoundException.class, () -> {
            wishService.deleteWish(999L, 1L);
        });

        assertEquals(WISH_NOT_FOUND + "999", exception.getMessage());
    }

    @Test
    @DisplayName("위시리스트에서 다른 사용자의 상품 삭제 시도")
    public void testDeleteWishPermissionDenied() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Member member = new Member(1L, "test@example.com", "password");
        Product product = new Product(1L, "Product", 100, "imageUrl", category);
        Wish wish = new Wish(1L, member, product);

        when(wishRepository.findById(1L)).thenReturn(Optional.of(wish));

        PermissionDeniedException exception = assertThrows(PermissionDeniedException.class, () -> {
            wishService.deleteWish(1L, 2L);
        });

        assertEquals(PERMISSION_DENIED, exception.getMessage());
    }

    @Test
    @DisplayName("회원의 위시리스트 조회 테스트 (페이지네이션 적용)")
    public void testGetWishlistByMemberId() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Member member = new Member(1L, "test@example.com", "password");
        Product product = new Product(1L, "Product", 100, "imageUrl", category);
        Wish wish = new Wish(1L, member, product);
        Pageable pageable = PageRequest.of(0, 10);
        when(wishRepository.findAllByMember_Id(1L, pageable))
            .thenReturn(new PageImpl<>(List.of(wish), pageable, 1));

        Page<WishResponse> wishlist = wishService.getWishlistByMemberId(1L, pageable);
        assertEquals(1, wishlist.getTotalElements());
        assertEquals(1L, wishlist.getContent().getFirst().memberId());
        assertEquals(1L, wishlist.getContent().getFirst().productId());
    }
}
