package gift.wish.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import gift.member.domain.Member;
import gift.member.persistence.MemberRepository;
import gift.product.domain.Category;
import gift.product.domain.Product;
import gift.product.exception.product.ProductNotFoundException;
import gift.product.persistence.ProductRepository;
import gift.wish.domain.Wish;
import gift.wish.exception.WishCanNotModifyException;
import gift.wish.persistence.WishRepository;
import gift.wish.service.command.WishCommand;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WishServiceTest {
    @Mock
    private WishRepository wishRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WishService wishService;

    @Test
    @DisplayName("WishService Wish 생성 테스트[성공]")
    void saveWishTest() {
        //given
        WishCommand wishCommand = new WishCommand(1L, 1L, 10);
        Category category = new Category(1L, "카테고리", "카테고리 설명", "카테고리 이미지", "카테고리 썸네일 이미지");
        Product product = new Product("테스트 상품", 1000, "http://test.com", category);
        Member memeber = new Member("test@test.com", "test", false);
        given(productRepository.findById(any())).willReturn(Optional.of(product));
        given(memberRepository.getReferenceById(any())).willReturn(memeber);

        //when
        wishService.saveWish(wishCommand);

        //then
        then(wishRepository).should().save(any(Wish.class));
        then(productRepository).should().findById(any());
        then(memberRepository).should().getReferenceById(any());
    }

    @Test
    @DisplayName("WishService Wish 생성 테스트[실패]")
    void saveWishWithNoProductTest() {
        // given
        WishCommand wishCommand = new WishCommand(1L, 1L, 10);
        given(productRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThrows(ProductNotFoundException.class, () -> wishService.saveWish(wishCommand));
    }

    @Test
    @DisplayName("WishService Wish 수정 테스트[성공]")
    void modifyWishTest() {
        // given
        WishCommand wishCommand = new WishCommand(1L, 1L, 5);
        Product product = mock(Product.class);
        Member member = mock(Member.class);
        Wish wish = new Wish(10, product, member);

        given(wishRepository.findWishByIdWithUserAndProduct(any())).willReturn(Optional.of(wish));
        given(productRepository.getReferenceById(any())).willReturn(product);
        given(memberRepository.getReferenceById(any())).willReturn(member);
        given(product.getId()).willReturn(1L);
        given(member.getId()).willReturn(1L);

        //when
        wishService.updateWish(wishCommand, any());

        //then
        then(wishRepository).should().findWishByIdWithUserAndProduct(any());
        then(productRepository).should().getReferenceById(any());
        then(memberRepository).should().getReferenceById(any());
        assertEquals(wish.getAmount(), 5);
    }

    @Test
    @DisplayName("WishService Wish 수정 테스트[Product 불일치]")
    void modifyWishWithUnMatchProductTest() {
        WishCommand wishCommand = new WishCommand(1L, 1L, 5);
        Product existProduct = mock(Product.class);
        Member existMember = mock(Member.class);
        Wish wish = new Wish(10, existProduct, existMember);
        Product requestProduct = mock(Product.class);
        Member requestMember = mock(Member.class);

        // given
        given(existMember.getId()).willReturn(1L);
        given(requestMember.getId()).willReturn(2L);
        given(wishRepository.findWishByIdWithUserAndProduct(any())).willReturn(Optional.of(wish));
        given(productRepository.getReferenceById(any())).willReturn(requestProduct);
        given(memberRepository.getReferenceById(any())).willReturn(requestMember);

        // when & then
        assertThrows(WishCanNotModifyException.class, () -> wishService.updateWish(wishCommand, any()));
    }

    @Test
    @DisplayName("WishService Wish 수정 테스트[Member 불일치]")
    void modifyWishWithUnMatchMemberTest() {
        WishCommand wishCommand = new WishCommand(1L, 1L, 5);
        Product existProduct = mock(Product.class);
        Member existMember = mock(Member.class);
        Wish wish = new Wish(10, existProduct, existMember);
        Product requestProduct = mock(Product.class);
        Member requestMember = mock(Member.class);

        // given
        given(existMember.getId()).willReturn(1L);
        given(requestMember.getId()).willReturn(1L);
        given(existProduct.getId()).willReturn(1L);
        given(requestProduct.getId()).willReturn(2L);
        given(wishRepository.findWishByIdWithUserAndProduct(any())).willReturn(Optional.of(wish));
        given(productRepository.getReferenceById(any())).willReturn(requestProduct);
        given(memberRepository.getReferenceById(any())).willReturn(requestMember);

        // when & then
        assertThrows(WishCanNotModifyException.class, () -> wishService.updateWish(wishCommand, any()));
    }
}