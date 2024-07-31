package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import gift.converter.StringToUrlConverter;
import gift.domain.Member;
import gift.domain.Product;
import gift.domain.ProductOption;
import gift.domain.WishProduct;
import gift.domain.WishProduct.Builder;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishProductRepository;
import gift.web.dto.request.wishproduct.CreateWishProductRequest;
import gift.web.dto.request.wishproduct.UpdateWishProductRequest;
import gift.web.dto.response.wishproduct.CreateWishProductResponse;
import gift.web.dto.response.wishproduct.ReadAllWishProductsResponse;
import gift.web.dto.response.wishproduct.UpdateWishProductResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WishProductServiceTest {

    @InjectMocks
    private WishProductService wishProductService;

    @Mock
    private WishProductRepository wishProductRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProductRepository productRepository;

    @Nested
    @DisplayName("createWishProduct 메서드는")
    class Describe_createWishProduct {

        @Test
        @DisplayName("이미 위시 상품이 존재할 경우, 수량만 추가한다.")
        void it_adds_quantity_when_wish_product_already_exists() {
            // given
            int beforeQuantity = 1;
            WishProduct wishProduct = new Builder().product(null).member(null).quantity(beforeQuantity).build();
            given(wishProductRepository.findByMemberIdAndProductId(any(), any()))
                .willReturn(Optional.of(wishProduct));

            int addQuantity = 2;
            CreateWishProductRequest request = new CreateWishProductRequest(1L, addQuantity);

            // when
            CreateWishProductResponse response = wishProductService.createWishProduct(1L, request);

            // then
            int expectedQuantity = beforeQuantity + addQuantity;
            assertAll(
                () -> assertThat(response.getId()).isEqualTo(wishProduct.getId()),
                () -> assertThat(response.getQuantity()).isEqualTo(expectedQuantity)
            );
        }

        @Test
        @DisplayName("새로운 위시 상품을 추가한다.")
        void it_adds_new_wish_product() {
            // given
            int quantity = 1;
            CreateWishProductRequest request = new CreateWishProductRequest(1L, quantity);
            given(wishProductRepository.findByMemberIdAndProductId(any(), any()))
                .willReturn(Optional.empty());

            given(wishProductRepository.save(any()))
                .willReturn(new WishProduct.Builder().id(1L).quantity(quantity).build());

            Member member = new Member.Builder().id(1L).build();
            given(memberRepository.findById(any()))
                .willReturn(Optional.of(member));

            Product product = new Product.Builder().id(1L).productOptions(List.of(new ProductOption.Builder().id(1L).build())).build();
            given(productRepository.findById(any()))
                .willReturn(Optional.of(product));

            // when
            CreateWishProductResponse response = wishProductService.createWishProduct(1L, request);

            // then
            assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getQuantity()).isEqualTo(quantity)
            );
        }
    }

    @Test
    @DisplayName("정상 요청일 때, 위시 리스트에 있는 모든 상품을 조회한다.")
    void readAllWishProducts() {
        //given
        Product product01 = new Product.Builder().id(1L).name("상품01").imageUrl(StringToUrlConverter.convert("https://www.google.com")).price(1000).productOptions(List.of(new ProductOption.Builder().id(1L).build())).build();
        Product product02 = new Product.Builder().id(2L).name("상품02").imageUrl(StringToUrlConverter.convert("https://www.google.com")).price(2000).productOptions(List.of(new ProductOption.Builder().id(2L).build())).build();

        WishProduct wishProduct01 = new Builder().id(1L).quantity(1).product(product01).build();
        WishProduct wishProduct02 = new Builder().id(2L).quantity(2).product(product02).build();

        List<WishProduct> wishProducts = List.of(wishProduct01, wishProduct02);
        given(wishProductRepository.findByMemberId(any(), any()))
            .willReturn(wishProducts);

        //when
        ReadAllWishProductsResponse response = wishProductService.readAllWishProducts(1L, null);

        //then
        assertAll(
            () -> assertThat(response.getWishlist().size()).isEqualTo(wishProducts.size()),
            () -> assertThat(response.getWishlist().get(0).getId()).isEqualTo(wishProduct01.getId()),
            () -> assertThat(response.getWishlist().get(0).getQuantity()).isEqualTo(wishProduct01.getQuantity()),
            () -> assertThat(response.getWishlist().get(0).getProductId()).isEqualTo(wishProduct01.getProduct().getId()),
            () -> assertThat(response.getWishlist().get(0).getName()).isEqualTo(wishProduct01.getProduct().getName()),
            () -> assertThat(response.getWishlist().get(0).getImageUrl()).isEqualTo(wishProduct01.getProduct().getImageUrl().toString()),
            () -> assertThat(response.getWishlist().get(0).getPrice()).isEqualTo(wishProduct01.getProduct().getPrice()),
            () -> assertThat(response.getWishlist().get(1).getId()).isEqualTo(wishProduct02.getId()),
            () -> assertThat(response.getWishlist().get(1).getQuantity()).isEqualTo(wishProduct02.getQuantity()),
            () -> assertThat(response.getWishlist().get(1).getId()).isEqualTo(wishProduct02.getProduct().getId()),
            () -> assertThat(response.getWishlist().get(1).getName()).isEqualTo(wishProduct02.getProduct().getName()),
            () -> assertThat(response.getWishlist().get(1).getImageUrl()).isEqualTo(wishProduct02.getProduct().getImageUrl().toString()),
            () -> assertThat(response.getWishlist().get(1).getPrice()).isEqualTo(wishProduct02.getProduct().getPrice())
        );
    }

    @Test
    @DisplayName("정상 요청일 때, 위시 리스트에 있는 상품의 수량을 수정한다.")
    void updateWishProduct() {
        //given
        Product product01 = new Product.Builder().id(1L).name("상품01").imageUrl(StringToUrlConverter.convert("https://www.google.com")).price(1000).productOptions(List.of(new ProductOption.Builder().id(1L).build())).build();
        WishProduct wishProduct01 = new Builder().id(1L).quantity(1).product(product01).build();
        given(wishProductRepository.findById(any()))
            .willReturn(Optional.of(wishProduct01));

        int quantity = 5;
        UpdateWishProductRequest request = new UpdateWishProductRequest(quantity);
        //when
        UpdateWishProductResponse response = wishProductService.updateWishProduct(1L, request);

        //then
        assertAll(
            () -> assertThat(response.getId()).isEqualTo(wishProduct01.getId()),
            () -> assertThat(response.getQuantity()).isEqualTo(quantity)
        );
    }

    @Test
    @DisplayName("정상 요청일 때, 위시 리스트에 있는 상품을 삭제한다.")
    void deleteWishProduct() {
        //given
        WishProduct wishProduct = new Builder().id(1L).build();
        given(wishProductRepository.findById(any()))
            .willReturn(Optional.of(wishProduct));

        //when
        assertDoesNotThrow(() -> wishProductService.deleteWishProduct(1L));
    }
}