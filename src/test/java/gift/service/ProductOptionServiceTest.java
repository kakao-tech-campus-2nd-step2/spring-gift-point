package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import gift.domain.Product;
import gift.domain.ProductOption;
import gift.domain.ProductOption.Builder;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import gift.web.dto.request.productoption.CreateProductOptionRequest;
import gift.web.dto.request.productoption.SubtractProductOptionQuantityRequest;
import gift.web.dto.request.productoption.UpdateProductOptionRequest;
import gift.web.dto.response.productoption.CreateProductOptionResponse;
import gift.web.dto.response.productoption.ReadAllProductOptionsResponse;
import gift.web.dto.response.productoption.SubtractProductOptionQuantityResponse;
import gift.web.dto.response.productoption.UpdateProductOptionResponse;
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
class ProductOptionServiceTest {

    @InjectMocks
    private ProductOptionService productOptionService;

    @Mock
    private ProductOptionRepository productOptionRepository;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 옵션 생성 요청이 정상적일 때, 상품 옵션을 성공적으로 생성합니다.")
    void createOption() {
        //given
        Long productId = 1L;
        CreateProductOptionRequest request = new CreateProductOptionRequest("optionName", 1000);
        given(productOptionRepository.save(any())).willReturn(request.toEntity(productId));
        given(productRepository.findById(any())).willReturn(Optional.of(new Product.Builder().productOptions(List.of(request.toEntity(productId))).build()));

        //when
        CreateProductOptionResponse response = productOptionService.createOption(productId, request);

        //then
        assertAll(
            () -> assertThat(response.getName()).isEqualTo(request.getName()),
            () -> assertThat(response.getStock()).isEqualTo(request.getStock())
        );
    }

    @Test
    @DisplayName("상품 옵션 생성 요청이 중복된 이름일 때, 예외를 발생시킵니다.")
    void createInitialOptions() {
        //given
        List<CreateProductOptionRequest> request = List.of(
            new CreateProductOptionRequest("optionName", 1000),
            new CreateProductOptionRequest("optionName", 1000));

        //when
        //then
        assertThatThrownBy(() -> productOptionService.createInitialOptions(1L, request))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("상품 옵션 조회 요청이 정상적일 때, 상품 옵션을 성공적으로 조회합니다.")
    void readAllOptions() {
        //given
        ProductOption option01 = new Builder().productId(1L).name("optionName").stock(1000).build();
        ProductOption option02 = new Builder().productId(1L).name("optionName").stock(1000).build();
        given(productOptionRepository.findAllByProductId(1L)).willReturn(List.of(option01, option02));

        //when
        ReadAllProductOptionsResponse response = productOptionService.readAllOptions(1L);

        //then
        assertAll(
            () -> assertThat(response.getOptions().get(0).getName()).isEqualTo(option01.getName()),
            () -> assertThat(response.getOptions().get(0).getStock()).isEqualTo(option01.getStock()),
            () -> assertThat(response.getOptions().get(1).getName()).isEqualTo(option02.getName()),
            () -> assertThat(response.getOptions().get(1).getStock()).isEqualTo(option02.getStock())
        );
    }

    @Test
    @DisplayName("상품 옵션 수정 요청이 정상적일 때, 상품 옵션을 성공적으로 수정합니다.")
    void updateOption() {
        //given
        Long productId = 1L;
        Long optionId = 1L;
        UpdateProductOptionRequest request = new UpdateProductOptionRequest("optionName", 1000);
        ProductOption option = new Builder().productId(productId).name("optionName").stock(1000).build();
        given(productOptionRepository.findById(optionId)).willReturn(Optional.of(option));

        //when
        UpdateProductOptionResponse response = productOptionService.updateOption(productId, optionId, request);

        //then
        assertAll(
            () -> assertThat(response.getName()).isEqualTo(request.getName()),
            () -> assertThat(response.getStock()).isEqualTo(request.getStock())
        );
    }

    @Nested
    @DisplayName("subtractOptionStock 메서드는")
    class SubtractOptionStock {

        @Test
        @DisplayName("요청 수량이 재고보다 적을 때, 재고를 감소시킨다.")
        void valid_request() {
            //given
            int requestedQuantity = 3;
            SubtractProductOptionQuantityRequest request = new SubtractProductOptionQuantityRequest(
                requestedQuantity);

            int stock = 10;
            ProductOption productOption = new Builder().stock(stock).build();
            given(productOptionRepository.findById(any())).willReturn(Optional.of(productOption));

            //when
            SubtractProductOptionQuantityResponse response = productOptionService.subtractOptionStock(
                1L, request);

            //then
            int expectedStock = stock - requestedQuantity;
            assertThat(response.getStock()).isEqualTo(expectedStock);
        }

        @Test
        @DisplayName("요청 수량이 재고보다 많을 때, 예외를 발생시킨다.")
        void invalid_request() {
            //given
            int requestedQuantity = 11;
            SubtractProductOptionQuantityRequest request = new SubtractProductOptionQuantityRequest(
                requestedQuantity);

            int stock = 10;
            ProductOption productOption = new Builder().stock(stock).build();
            given(productOptionRepository.findById(any())).willReturn(Optional.of(productOption));

            //when
            //then
            assertThatThrownBy(() -> productOptionService.subtractOptionStock(1L, request))
                .isInstanceOf(IllegalStateException.class);
        }
    }

    @Test
    @DisplayName("상품 옵션 삭제 요청이 정상적일 때, 상품 옵션을 성공적으로 삭제합니다.")
    void deleteOption() {
        //given
        Long optionId = 1L;

        //when
        given(productOptionRepository.findById(optionId)).willReturn(Optional.of(new ProductOption.Builder().build()));

        //then
        assertDoesNotThrow(() -> productOptionService.deleteOption(optionId));
    }

    @Test
    @DisplayName("상품 옵션 전체 삭제 요청이 정상적일 때, 상품 옵션을 성공적으로 전체 삭제합니다.")
    void deleteAllOptionsByProductId() {
        //given
        Long productId = 1L;

        //when
        //then
        assertDoesNotThrow(() -> productOptionService.deleteAllOptionsByProductId(productId));
    }
}