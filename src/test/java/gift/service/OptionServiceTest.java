package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import gift.domain.category.entity.Category;
import gift.domain.option.dto.OptionRequest;
import gift.domain.option.dto.OptionResponse;
import gift.domain.option.entity.Option;
import gift.domain.option.repository.OptionRepository;
import gift.domain.option.service.OptionService;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OptionServiceTest {

    @InjectMocks
    OptionService optionService;
    @Mock
    OptionRepository optionRepository;
    @Mock
    ProductRepository productRepository;

    @Test
    @DisplayName("특정 상품에 대한 옵션들 조회 테스트")
    void getProductOptions() {
        // given
        Product savedProduct = createProduct();
        List<Option> optionList = Arrays.asList(createOption(), createOption("name2"));

        doReturn(Optional.of(savedProduct)).when(productRepository).findById(any(Long.class));
        doReturn(optionList).when(optionRepository).findAllByProduct(any(Product.class));

        List<OptionResponse> expected = optionList.stream().map(this::entityToDto).toList();

        // when
        List<OptionResponse> actual = optionService.getProductOptions(savedProduct.getId());

        // then
        assertAll(
            () -> IntStream.range(0, actual.size()).forEach((i) -> {
                assertThat(actual.get(i).name())
                    .isEqualTo(expected.get(i).name());
                assertThat(actual.get(i).quantity())
                    .isEqualTo(expected.get(i).quantity());
            })
        );
    }

    @Test
    @DisplayName("특정 상품에 옵션 추가 테스트")
    void addOptionToProduct() {
        Product savedProduct = createProduct();

        List<Option> optionList = Arrays.asList(createOption(), createOption("name2"));

        Option newOption = createOption("newOption");

        doReturn(Optional.of(savedProduct)).when(productRepository).findById(any(Long.class));
        doReturn(optionList).when(optionRepository).findAllByProduct(any(Product.class));
        doReturn(newOption).when(optionRepository).save(any(Option.class));

        OptionResponse expected = entityToDto(newOption);

        // when
        OptionResponse actual = optionService.addOptionToProduct(savedProduct.getId(),
            new OptionRequest("newOption", 1000));

        assertAll(
            () -> assertThat(actual.name()).isEqualTo(expected.name()),
            () -> assertThat(actual.quantity()).isEqualTo(expected.quantity())
        );
    }

    private OptionResponse entityToDto(Option option) {
        return new OptionResponse(option.getId(), option.getName(), option.getQuantity());
    }

    private Product createProduct() {
        return createProduct(1L, new Category(1L, "test", "color", "image", "description"));
    }

    private Product createProduct(Long id, Category category) {
        return new Product(id, "test", 1000, "test.jpg", category);
    }

    private Option createOption() {
        return createOption("name");
    }

    private Option createOption(String name) {
        return new Option(name, 1000, createProduct());
    }
}
