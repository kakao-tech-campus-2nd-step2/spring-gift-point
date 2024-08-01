package gift.service;

import gift.dto.wishproduct.WishProductRequest;
import gift.exception.AlreadyExistsException;
import gift.exception.NotFoundElementException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class WishProductServiceTest {

    @Autowired
    private WishProductService wishProductService;

    @Test
    @DisplayName("위시 리스트 상품 추가하기")
    void successAddWishProduct() {
        //given
        var wishProductAddRequest = new WishProductRequest(1L);
        Assertions.assertThat(wishProductService.getWishProducts(1L, PageRequest.of(0, 10)).content().size()).isEqualTo(0);
        //when
        var wishProduct = wishProductService.addWishProduct(wishProductAddRequest, 1L);
        //then
        Assertions.assertThat(wishProductService.getWishProducts(1L, PageRequest.of(0, 10)).content().size()).isEqualTo(1);

        wishProductService.deleteWishProduct(wishProduct.id());
    }

    @Test
    @DisplayName("위시 리스트 상품 삭제하기")
    void successDeleteWishProduct() {
        //given
        var wishProductAddRequest = new WishProductRequest(1L);
        var wishProduct = wishProductService.addWishProduct(wishProductAddRequest, 1L);
        Assertions.assertThat(wishProductService.getWishProducts(1L, PageRequest.of(0, 10)).content().size()).isEqualTo(1);
        //when
        wishProductService.deleteWishProduct(wishProduct.id());
        //then
        Assertions.assertThat(wishProductService.getWishProducts(1L, PageRequest.of(0, 10)).content().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("이용자끼리 위시 리스트가 다르다")
    void successGetDifferentWishProducts() {
        //given
        var wishProduct1AddRequest = new WishProductRequest(1L);
        var wishProduct2AddRequest = new WishProductRequest(2L);
        var managerWishProduct1 = wishProductService.addWishProduct(wishProduct1AddRequest, 1L);
        var managerWishProduct2 = wishProductService.addWishProduct(wishProduct2AddRequest, 1L);
        //when
        var memberWishProducts = wishProductService.getWishProducts(2L, PageRequest.of(0, 10));
        //then
        Assertions.assertThat(memberWishProducts.content().size()).isEqualTo(0);

        wishProductService.deleteWishProduct(managerWishProduct1.id());
        wishProductService.deleteWishProduct(managerWishProduct2.id());
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID 로 위시 리스트 상품 추가 요청시 예외 발생")
    void failAddWishProductWithNotExistsProductId() {
        //given
        var invalidWishProductAddRequest = new WishProductRequest(10L);
        //when, then
        Assertions.assertThatThrownBy(() -> wishProductService.addWishProduct(invalidWishProductAddRequest, 1L))
                .isInstanceOf(NotFoundElementException.class);
    }

    @Test
    @DisplayName("이미 존재하는 상품을 위시 리스트에 추가하면 예외가 발생한다.")
    void failAddWishProductAlreadyExistsWishProduct() {
        //given
        var wishProduct1AddRequest = new WishProductRequest(1L);
        var wishProduct = wishProductService.addWishProduct(wishProduct1AddRequest, 1L);
        //when, then
        Assertions.assertThatThrownBy(() -> wishProductService.addWishProduct(wishProduct1AddRequest, 1L)).isInstanceOf(AlreadyExistsException.class);

        wishProductService.deleteWishProduct(wishProduct.id());
    }

    @Test
    @DisplayName("2개의 상품이 추가된 상황에서 size 가 1인 페이지로 조회하면 결과의 길이는 1이다.")
    void successGetProductsWishPageSizeOne() {
        //given
        var wishProduct1AddRequest = new WishProductRequest(1L);
        var wishProduct1 = wishProductService.addWishProduct(wishProduct1AddRequest, 1L);
        var wishProduct2AddRequest = new WishProductRequest(2L);
        var wishProduct2 = wishProductService.addWishProduct(wishProduct2AddRequest, 1L);
        //when
        var wishProducts = wishProductService.getWishProducts(1L, PageRequest.of(0, 1));
        //then
        Assertions.assertThat(wishProducts.content().size()).isEqualTo(1);

        wishProductService.deleteWishProduct(wishProduct1.id());
        wishProductService.deleteWishProduct(wishProduct2.id());
    }
}
