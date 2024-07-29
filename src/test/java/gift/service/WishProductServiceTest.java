package gift.service;

import gift.dto.wishproduct.WishProductAddRequest;
import gift.dto.wishproduct.WishProductUpdateRequest;
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
        var wishProductAddRequest = new WishProductAddRequest(1L, 5);
        Assertions.assertThat(wishProductService.getWishProducts(1L, PageRequest.of(0, 10)).size()).isEqualTo(0);
        //when
        var wishProduct = wishProductService.addWishProduct(wishProductAddRequest, 1L);
        //then
        Assertions.assertThat(wishProductService.getWishProducts(1L, PageRequest.of(0, 10)).size()).isEqualTo(1);

        wishProductService.deleteWishProduct(wishProduct.id());
    }

    @Test
    @DisplayName("위시 리스트 상품 삭제하기")
    void successDeleteWishProduct() {
        //given
        var wishProductAddRequest = new WishProductAddRequest(1L, 5);
        var wishProduct = wishProductService.addWishProduct(wishProductAddRequest, 1L);
        Assertions.assertThat(wishProductService.getWishProducts(1L, PageRequest.of(0, 10)).size()).isEqualTo(1);
        //when
        wishProductService.deleteWishProduct(wishProduct.id());
        //then
        Assertions.assertThat(wishProductService.getWishProducts(1L, PageRequest.of(0, 10)).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("위시리스트 수량 0으로 변경하기")
    void successUpdateWishProductWithZeroQuantity() {
        //given
        var wishProductAddRequest = new WishProductAddRequest(1L, 5);
        var wishProduct = wishProductService.addWishProduct(wishProductAddRequest, 1L);
        Assertions.assertThat(wishProductService.getWishProducts(1L, PageRequest.of(0, 10)).size()).isEqualTo(1);
        var wishProductUpdateRequest = new WishProductUpdateRequest(0);
        //when
        wishProductService.updateWishProduct(wishProduct.id(), wishProductUpdateRequest);
        //then
        Assertions.assertThat(wishProductService.getWishProducts(1L, PageRequest.of(0, 10)).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("이용자끼리 위시 리스트가 다르다")
    void successGetDifferentWishProducts() {
        //given
        var wishProduct1AddRequest = new WishProductAddRequest(1L, 5);
        var wishProduct2AddRequest = new WishProductAddRequest(2L, 5);
        var managerWishProduct1 = wishProductService.addWishProduct(wishProduct1AddRequest, 1L);
        var managerWishProduct2 = wishProductService.addWishProduct(wishProduct2AddRequest, 1L);
        //when
        var memberWishProducts = wishProductService.getWishProducts(2L, PageRequest.of(0, 10));
        //then
        Assertions.assertThat(memberWishProducts.size()).isEqualTo(0);

        wishProductService.deleteWishProduct(managerWishProduct1.id());
        wishProductService.deleteWishProduct(managerWishProduct2.id());
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID 로 위시 리스트 상품 추가 요청시 예외 발생")
    void failAddWishProductWithNotExistsProductId() {
        //given
        var invalidWishProductAddRequest = new WishProductAddRequest(10L, 5);
        //when, then
        Assertions.assertThatThrownBy(() -> wishProductService.addWishProduct(invalidWishProductAddRequest, 1L))
                .isInstanceOf(NotFoundElementException.class);
    }

    @Test
    @DisplayName("이미 존재하는 상품 위시 리스트에 추가시 수량 변경")
    void successAddWishProductAlreadyExistsWishProduct() {
        //given
        var wishProduct1AddRequest = new WishProductAddRequest(1L, 5);
        wishProductService.addWishProduct(wishProduct1AddRequest, 1L);
        //when
        var wishProduct = wishProductService.addWishProduct(wishProduct1AddRequest, 1L);
        //then
        var wishProducts = wishProductService.getWishProducts(1L, PageRequest.of(0, 10));
        Assertions.assertThat(wishProducts.size()).isEqualTo(1);
        Assertions.assertThat(wishProducts.get(0).quantity()).isEqualTo(10);

        wishProductService.deleteWishProduct(wishProduct.id());
    }

    @Test
    @DisplayName("2개의 상품이 추가된 상황에서 size 가 1인 페이지로 조회하면 결과의 길이는 1이다.")
    void successGetProductsWishPageSizeOne() {
        //given
        var wishProduct1AddRequest = new WishProductAddRequest(1L, 5);
        var wishProduct1 = wishProductService.addWishProduct(wishProduct1AddRequest, 1L);
        var wishProduct2AddRequest = new WishProductAddRequest(2L, 5);
        var wishProduct2 = wishProductService.addWishProduct(wishProduct2AddRequest, 1L);
        //when
        var wishProducts = wishProductService.getWishProducts(1L, PageRequest.of(0, 1));
        //then
        Assertions.assertThat(wishProducts.size()).isEqualTo(1);

        wishProductService.deleteWishProduct(wishProduct1.id());
        wishProductService.deleteWishProduct(wishProduct2.id());
    }
}
