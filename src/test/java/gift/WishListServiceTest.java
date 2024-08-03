package gift;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import gift.category.dto.CategoryResponseDto;
import gift.global.dto.PageInfoDto;
import gift.option.dto.OptionResponseDto;
import gift.product.dto.FullOptionsProductResponseDto;
import gift.product.entity.Product;
import gift.product.service.ProductService;
import gift.wishlist.dto.WishListResponseDto;
import gift.wishlist.entity.WishList;
import gift.wishlist.repository.WishListRepository;
import gift.wishlist.service.WishListService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class WishListServiceTest {

    @InjectMocks
    private WishListService wishListService;
    @Mock
    private WishListRepository wishListRepository;
    @Mock
    private ProductService productService;

    /*
     * - [ ] 정상적인 경우
     * - [ ] 이미 담은 상품인 경우
     */
    @Test
    public void insertWishListTest() {
        // given
        var productId = 1L;
        var name = "초콜릿";
        var price = 3000;
        var imageUrl = "choco.png";
        var categoryResponseDto = new CategoryResponseDto(1L, "간식", "gansik.png");
        var optionResponseDto = new OptionResponseDto(1L, "화이트초콜릿", 10000, productId);
        var fullOptionsProductResponseDto = new FullOptionsProductResponseDto(productId, name,
            price, imageUrl, categoryResponseDto, List.of(optionResponseDto));
        var userId = 1;
        var invalidProductId = 2L;
        var invalidFullOptionsProductResponseDto = new FullOptionsProductResponseDto(
            invalidProductId, name, price, imageUrl, categoryResponseDto,
            List.of(optionResponseDto));
        var product = fullOptionsProductResponseDto.toProduct();
        var invalidProduct = invalidFullOptionsProductResponseDto.toProduct();
        var wishList = new WishList(userId, product);

        given(productService.selectFullOptionProduct(productId)).willReturn(
            fullOptionsProductResponseDto);
        given(productService.selectFullOptionProduct(invalidProductId)).willReturn(
            invalidFullOptionsProductResponseDto);
        given(wishListRepository.existsByUserIdAndProduct(any(Long.class),
            any(Product.class))).willAnswer(invocation -> {
            Product actualProduct = invocation.getArgument(1);
            if (actualProduct.getProductId() == productId) {
                return false;
            }
            return true;
        });
        given(wishListRepository.save(any(WishList.class))).willReturn(wishList);

        // when, then
        // 정상적인 경우
        Assertions.assertThatCode(() -> wishListService.insertWishProduct(productId, userId))
            .doesNotThrowAnyException();

        // 이미 담은 상품인 경우
        Assertions.assertThatThrownBy(
                () -> wishListService.insertWishProduct(invalidProductId, userId))
            .isInstanceOf(IllegalArgumentException.class);

        // 정상적인 경우
        then(productService).should().selectFullOptionProduct(productId);
        then(wishListRepository).should().save(any(WishList.class));

        // 이미 담은 상품인 경우
        then(productService).should().selectFullOptionProduct(invalidProductId);

        // 정상, 이미 담은 상품
        then(wishListRepository).should(times(2))
            .existsByUserIdAndProduct(any(Long.class), any(Product.class));
    }

    /*
     * - [ ] 정상적인 경우
     */
    @Test
    public void readWishProductsTest() {
        // given
        var productId = 1L;
        var name = "초콜릿";
        var productId2 = 2L;
        var name2 = "큰 초콜릿";
        var price = 3000;
        var imageUrl = "choco.png";
        var categoryResponseDto = new CategoryResponseDto(1L, "간식", "gansik.png");
        var optionResponseDto = new OptionResponseDto(1L, "화이트초콜릿", 10000, productId);
        var optionResponseDto2 = new OptionResponseDto(2L, "큰 화이트초콜릿", 10000, productId2);
        var fullOptionsProductResponseDto = new FullOptionsProductResponseDto(productId, name,
            price, imageUrl, categoryResponseDto, List.of(optionResponseDto));
        var fullOptionsProductResponseDto2 = new FullOptionsProductResponseDto(productId2, name2,
            price, imageUrl, categoryResponseDto, List.of(optionResponseDto2));
        var product = fullOptionsProductResponseDto.toProduct();
        var product2 = fullOptionsProductResponseDto2.toProduct();
        var userId = 1;
        var wishList = new WishList(1L, userId, product);
        var wishList2 = new WishList(2L, userId, product2);
        var wishListList = List.of(wishList, wishList2);
        var pageNumber = 0;
        var pageSize = 10;
        var sortProperty = "product_price";
        var sortDirection = "DESC";
        var pageInfoDto = new PageInfoDto(pageNumber, pageSize, sortProperty, sortDirection);
        var pageRequest = pageInfoDto.toPageInfo().toPageRequest();
        var wishListPage = new PageImpl<>(wishListList, pageRequest, wishListList.size());

        given(wishListRepository.findByUserId(any(Long.class), any(PageRequest.class))).willReturn(
            wishListPage);

        // when, then
        Assertions.assertThat(wishListService.readWishProducts(userId, pageInfoDto))
            .isEqualTo(wishListPage.stream().map(
                WishListResponseDto::fromWishList).toList());

        // 정상적인 경우
        then(wishListRepository).should().findByUserId(any(Long.class), any(PageRequest.class));
    }

    /*
     * - [ ] 정상적인 경우
     * - [ ] 존재하지 않는 제품을 삭제하려는 경우
     * - [ ] 다른 유저의 제품을 삭제하려는 경우
     */
    @Test
    public void deleteWishProductTest() {
        // given
        var productId = 1L;
        var name = "초콜릿";
        var price = 3000;
        var imageUrl = "choco.png";
        var categoryResponseDto = new CategoryResponseDto(1L, "간식", "gansik.png");
        var optionResponseDto = new OptionResponseDto(1L, "화이트초콜릿", 10000, productId);
        var fullOptionsProductResponseDto = new FullOptionsProductResponseDto(productId, name,
            price, imageUrl, categoryResponseDto, List.of(optionResponseDto));
        var product = fullOptionsProductResponseDto.toProduct();
        var userId = 1L;
        var invalidUserId = 2;
        var wishListId = 1L;
        var invalidWishListId = 2L;
        var wishList = new WishList(wishListId, userId, product);

        given(wishListRepository.findById(wishListId)).willReturn(Optional.of(wishList));
        given(wishListRepository.findById(invalidWishListId)).willReturn(Optional.empty());

        // when, then
        // 정상적인 경우
        Assertions.assertThatCode(() -> wishListService.deleteWishProduct(wishListId, userId))
            .doesNotThrowAnyException();

        // 존재하지 않는 위시리스트 제거하는 경우
        Assertions.assertThatThrownBy(
            () -> wishListService.deleteWishProduct(invalidWishListId, userId)).isInstanceOf(
            NoSuchElementException.class);

        // 타인의 위시리스트 조작하는 경우
        Assertions.assertThatThrownBy(
                () -> wishListService.deleteWishProduct(wishListId, invalidUserId))
            .isInstanceOf(IllegalArgumentException.class);

        // 정상적인 경우, 타인의 위시리스트 조작하는 경우
        then(wishListRepository).should(times(2)).findById(wishListId);
        // 존재하지 않는 위시리스트 제거하는 경우
        then(wishListRepository).should().findById(invalidWishListId);
    }

}
