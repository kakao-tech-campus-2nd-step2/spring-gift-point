package gift.domain.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import gift.domain.product.dto.OptionRequest;
import gift.domain.product.dto.OptionResponse;
import gift.domain.product.entity.Category;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.OptionJpaRepository;
import gift.domain.product.repository.ProductJpaRepository;
import gift.exception.DuplicateOptionNameException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@AutoConfigureMockMvc
@SpringBootTest
class OptionServiceTest {

    @Autowired
    private OptionService optionService;

    @MockBean
    private OptionJpaRepository optionJpaRepository;

    @MockBean
    private ProductJpaRepository productJpaRepository;

    private static final Category category = new Category(1L, "교환권", "#FFFFFF", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "test");
    private static final Product product = new Product(1L, category, "testProduct", 10000, "https://test.com");

    @BeforeEach
    void setUp() {
        product.removeOptions();
    }

    @Test
    @DisplayName("옵션 생성 서비스 테스트")
    void create() {
        // given
        List<OptionRequest> optionRequestDtos = List.of(
            new OptionRequest("수박맛", 969),
            new OptionRequest("자두맛", 90)
        );
        given(optionJpaRepository.save(any(Option.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        optionService.create(product, optionRequestDtos);

        // then
        assertEquals(optionRequestDtos.size(), product.getOptions().size());
    }

    @Test
    @DisplayName("옵션 생성 서비스 중복 옵션 테스트")
    void create_fail() {
        // given
        product.addOption(new Option(1L, product, "자두맛", 80));

        OptionRequest optionRequestDto = new OptionRequest("자두맛", 969);

        given(optionJpaRepository.save(any(Option.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when & then
        assertThrows(DuplicateOptionNameException.class, () -> optionService.create(product, List.of(optionRequestDto)));
    }

    @Test
    @DisplayName("옵션 전체 조회 서비스 테스트")
    void readAll() {
        // given
        Option option = new Option(1L, product, "사과맛", 90);
        product.addOption(option);
        given(productJpaRepository.findById(anyLong())).willReturn(Optional.of(product));

        // when
        List<OptionResponse> actual = optionService.readAll(1L);

        // then
        assertAll(
            () -> assertThat(actual).hasSize(1),
            () -> assertThat(actual.get(0)).isEqualTo(OptionResponse.from(option))
        );
    }

    @Test
    @DisplayName("옵션 수정 서비스 테스트")
    void update() {
        // given
        OptionRequest optionRequest = new OptionRequest("수박맛", 969);
        product.addOption(optionRequest.toOption(product));

        OptionRequest optionUpdateDto = new OptionRequest("자두맛", 90);

        doNothing().when(optionJpaRepository).deleteAll(any());
        given(optionJpaRepository.save(any(Option.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        optionService.update(product, List.of(optionUpdateDto));

        // then
        assertEquals(1, product.getOptions().size());
    }

    @Test
    @DisplayName("옵션 수정 서비스 중복 옵션 테스트")
    void update_fail_duplicate_name() {
        // given
        List<OptionRequest> optionUpdateDtos = List.of(
            new OptionRequest("자두맛", 90),
            new OptionRequest("자두맛", 70)
        );

        given(optionJpaRepository.save(any(Option.class))).willAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(optionJpaRepository).deleteAll(any());

        // when & then
        assertThrows(DuplicateOptionNameException.class, () -> optionService.update(product, optionUpdateDtos));
    }
}