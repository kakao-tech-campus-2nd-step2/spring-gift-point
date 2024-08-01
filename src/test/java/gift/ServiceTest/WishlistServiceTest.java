package gift.ServiceTest;

import gift.Entity.*;
import gift.Mapper.Mapper;
import gift.Model.MemberDto;
import gift.Model.WishlistDto;
import gift.Repository.MemberJpaRepository;
import gift.Repository.ProductJpaRepository;
import gift.Repository.WishlistJpaRepository;
import gift.Service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class WishlistServiceTest {

    private WishlistService wishlistService;
    private final Mapper mapper = mock(Mapper.class);
    private final WishlistJpaRepository wishlistJpaRepository = mock(WishlistJpaRepository.class);
    private final MemberJpaRepository memberJpaRepository = mock(MemberJpaRepository.class);
    private final ProductJpaRepository productJpaRepository = mock(ProductJpaRepository.class);

    @BeforeEach
    void setUp() {
        wishlistService = new WishlistService(wishlistJpaRepository, mapper);
    }

    @Test
    public void testGetWishlist() {
        // given
        long memberId = 1;

        // when
        wishlistService.getWishlist(memberId);

        // then
        verify(wishlistJpaRepository, times(1)).findByMemberId(memberId);
    }

    @Test
    public void testGetWishlistByPage() {
        // given
        MemberDto memberDto = new MemberDto(1L, "1234@naver.com", "1234", false);
        Member member = new Member(1L, "1234@naver.com", "1234", false);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Wishlist> expectedPage = new PageImpl<>(Collections.emptyList());

        given(mapper.memberDtoToEntity(any(MemberDto.class))).willReturn(member);
        given(wishlistJpaRepository.findByMember(eq(member), eq(pageable))).willReturn(expectedPage);

        // when
        wishlistService.getWishlistByPage(memberDto, pageable);

        // then
        verify(wishlistJpaRepository, times(1)).findByMember(member, pageable);
    }

    @Test
    public void testAddWishlistItem() {
        // given
        Member member = new Member(1, "1234@naver.com", "1234", false);
        Product product = new Product(1L, "product1", null, 1000, "http://localhost:8080/image1.jpg");
        Option option = new Option(1L, product, "option1", 1);
        WishlistDto wishlistDto = new WishlistDto(1L, 1L, 5, 3, "product1", 0, 1L);
        Wishlist wishlist = new Wishlist(1L, member, product, "product1", 2, 5000, option);

        given(mapper.wishlistDtoToEntity(wishlistDto)).willReturn(wishlist);
        given(wishlistJpaRepository.save(wishlist)).willReturn(wishlist);

        // when
        wishlistService.addWishlistItem(wishlistDto);

        // then
        verify(wishlistJpaRepository, times(1)).save(wishlist);
    }

    @Test
    @DisplayName("남은 수량이 더 클 때")
    public void testDescendWishlistItem() {
        // given
        Member member = new Member(1, "1234@naver.com", "1234", false);
        Product product = new Product(1L, "product1", null, 1000, "http://localhost:8080/image1.jpg");
        Option option = new Option(1L, product, "option1", 1);
        WishlistDto wishlistDto = new WishlistDto(1L, 1L, 5, 3, "product1", 0, 1L);
        Wishlist wishlist = new Wishlist(1L, member, product, "product1", 2, 5000, option);
        given(wishlistJpaRepository.save(wishlist)).willReturn(wishlist);

        // when
        wishlistService.removeWishlistItem(1L);

        // then
        verify(wishlistJpaRepository, times(1)).save(wishlist);
        verify(wishlistJpaRepository, times(0)).delete(wishlist);
    }

    @Test
    @DisplayName("뺄 수량이 더 클 때")
    public void testRemoveWishlistItem() {
        // given
        Member member = new Member(1, "1234@naver.com", "1234",  false);
        Product product = new Product(1L, "product1", null, 1000, "http://localhost:8080/image1.jpg");
        Option option = new Option(1L, product, "option1", 1);
        WishlistDto wishlistDto = new WishlistDto(1L, 1L, 0, 6, "product1", 0, 1L);
        Wishlist wishlist = new Wishlist(1L, member, product, "product1", 5, 5000, option);
        given(wishlistJpaRepository.save(wishlist)).willReturn(wishlist);

        // when
        wishlistService.removeWishlistItem(1L);

        // then
        verify(wishlistJpaRepository, times(0)).save(wishlist);
        verify(wishlistJpaRepository, times(1)).delete(wishlist);
    }

}
