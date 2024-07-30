package gift.service;

import gift.model.option.Option;
import gift.model.option.OptionRequest;
import gift.model.option.OptionResponse;
import gift.model.product.Product;
import gift.repository.option.OptionRepository;
import gift.repository.product.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OptionServiceTest {

    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OptionService optionService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = productRepository.save(new Product("아메리카노", 4500, "americano", null));
    }
    @Test
    @DisplayName("옵션 추가 Test")
    void addOption() {
        // given
        OptionRequest optionRequest = new OptionRequest("옵션", 100);

        // when
        OptionResponse optionResponse = optionService.addOption(1L, optionRequest);

        // then
        Assertions.assertThat(optionResponse.name()).isEqualTo("옵션");
        Assertions.assertThat(optionResponse.quantity()).isEqualTo(100);
    }

    @Test
    @DisplayName("상품 Id에 따른 옵션 조회 Test")
    void getOptionsByProudctId() {
        // given
        Option option = optionRepository.save(new Option("옵션1", 100, product));

        // when
        List<OptionResponse> responses = optionService.getOptionByProductId(product.getId());

        // then
        Assertions.assertThat(responses).hasSize(1);
        Assertions.assertThat(responses.get(0).name()).isEqualTo("옵션1");
    }

    @Test
    @DisplayName("옵션 수정 Test")
    void updateOption() {
        // given
        Option option = optionRepository.save(new Option("옵션1", 100, product));
        OptionRequest request = new OptionRequest("수정된 옵션", 50);

        // when
        OptionResponse response = optionService.updateOption(product.getId(), option.getId(), request);

        // then
        Assertions.assertThat(response.name()).isEqualTo("수정된 옵션");
        Assertions.assertThat(response.quantity()).isEqualTo(50);
    }

    @Test
    @DisplayName("옵션 삭제 Test")
    void deleteOption() {
        // given
        Option option = optionRepository.save(new Option("옵션1", 100, product));

        // when
        optionService.deleteOption(product.getId(), option.getId());
        Optional<Option> deletedOption = optionRepository.findById(option.getId());

        // then
        Assertions.assertThat(deletedOption).isEmpty();
    }


}
