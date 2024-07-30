package gift.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.category.model.Category;
import gift.common.auth.LoginMemberDto;
import gift.member.model.Member;
import gift.product.ProductRepository;
import gift.product.model.Product;
import gift.wish.WishRepository;
import gift.wish.WishService;
import gift.wish.model.Wish;
import gift.wish.model.WishRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class WishServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WishRepository wishRepository;

    private WishService wishService;

    @BeforeEach
    void setUp() {
        wishService = new WishService(wishRepository, productRepository);
    }

    @Test
    void getWishListTest() {
        given(wishRepository.findAllByMemberId(any(), any())).willReturn(Page.empty());
        LoginMemberDto loginMemberDto = getLoginMemberDto();

        wishService.getWishList(loginMemberDto, PageRequest.of(10, 10));
        then(wishRepository).should().findAllByMemberId(any(), any());
    }

    @Test
    void addProductTest() {
        given(productRepository.findById(any())).willReturn(Optional.of(
            new Product("gamza", 1000, "gamza.jpg", new Category("식품", "##111", "식품.jpg", "식품"))));
        WishRequest wishRequest = getWishRequestDto();
        LoginMemberDto loginMemberDto = getLoginMemberDto();

        wishService.addProductToWishList(wishRequest, loginMemberDto);

        then(wishRepository).should().save(any());
    }

    @Test
    void updateWishTest() {
        Wish wish = new Wish(new Member(1L, "member1@example.com", "member1", "user"), null, 1);
        given(wishRepository.findById(any())).willReturn(Optional.of(wish));
        WishRequest wishRequest = getWishRequestDto();
        LoginMemberDto loginMemberDto = getLoginMemberDto();

        wishService.updateProductInWishList(1L, wishRequest, loginMemberDto);

        assertThat(wish.getCount()).isEqualTo(3);
    }

    @Test
    void deleteWishTest() {
        given(wishRepository.findById(any())).willReturn(
            Optional.of(new Wish(new Member(1L, "email", "name", "role"), null, 1)));
        LoginMemberDto loginMemberDto = getLoginMemberDto();

        wishService.deleteProductInWishList(1L, loginMemberDto);

        then(wishRepository).should().deleteById(any());
    }

    private LoginMemberDto getLoginMemberDto() {
        return LoginMemberDto.from(
            new Member(1L, "member1@example.com", "member1", "user"));
    }

    private WishRequest getWishRequestDto() {
        WishRequest wishRequest = new WishRequest(1L, 3);
        return wishRequest;
    }


}
