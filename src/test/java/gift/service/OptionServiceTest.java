package gift.service;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.dto.request.OptionRequest;
import gift.dto.response.OptionResponse;
import gift.exception.DuplicateOptionNameException;
import gift.exception.InsufficientQuantityException;
import gift.exception.MinimumOptionRequiredException;
import gift.exception.ProductNotFoundException;
import gift.repository.option.OptionSpringDataJpaRepository;
import gift.repository.product.ProductSpringDataJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OptionServiceTest {

    @InjectMocks
    private OptionService optionService;

    @Mock
    private ProductSpringDataJpaRepository productRepository;

    @Mock
    private OptionSpringDataJpaRepository optionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void 옵션조회_제품이존재하지않는경우() {
        Long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(false);

        assertThrows(ProductNotFoundException.class,
                () -> optionService.getOptionsByProductId(productId),
                "제품이 존재하지 않아야 합니다.");

        verify(optionRepository, never()).findByProductId(productId);
    }

    @Test
    public void 옵션조회_제품이존재할경우() {
        Long productId = 1L;
        Option option1 = mock(Option.class);
        Option option2 = mock(Option.class);

        when(productRepository.existsById(productId)).thenReturn(true);
        when(optionRepository.findByProductId(productId)).thenReturn(Arrays.asList(option1, option2));

        when(option1.getId()).thenReturn(1L);
        when(option1.getName()).thenReturn("옵션1");
        when(option1.getQuantity()).thenReturn(10);

        when(option2.getId()).thenReturn(2L);
        when(option2.getName()).thenReturn("옵션2");
        when(option2.getQuantity()).thenReturn(20);

        List<OptionResponse> responses = optionService.getOptionsByProductId(productId);

        assertAll("옵션 조회 결과",
                () -> assertEquals(2, responses.size(), "옵션의 개수가 맞아야 합니다."),
                () -> assertEquals("옵션1", responses.get(0).getName(), "첫 번째 옵션 이름이 맞아야 합니다."),
                () -> assertEquals(10, responses.get(0).getQuantity(), "첫 번째 옵션 수량이 맞아야 합니다."),
                () -> assertEquals("옵션2", responses.get(1).getName(), "두 번째 옵션 이름이 맞아야 합니다."),
                () -> assertEquals(20, responses.get(1).getQuantity(), "두 번째 옵션 수량이 맞아야 합니다.")
        );
    }

    @Test
    void 옵션추가_제품이존재하지않는경우() {
        Long productId = 1L;
        OptionRequest optionRequest = new OptionRequest("새 옵션", 5);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> optionService.addOptionToProduct(productId, optionRequest),
                "제품이 존재하지 않아야 합니다.");
    }

    @Test
    public void 옵션추가_옵션이중등록시() {
        Long productId = 1L;
        OptionRequest optionRequest = new OptionRequest("중복 옵션", 5);
        Product product = mock(Product.class);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(optionRepository.findByNameAndProductId(optionRequest.getName(), 1L)).thenReturn(Optional.of(mock(Option.class)));

        assertThrows(DuplicateOptionNameException.class,
                () -> optionService.addOptionToProduct(productId, optionRequest),
                "옵션 이름이 중복되어야 합니다.");
    }

    @Test
    public void 옵션추가_성공() {
        Long productId = 1L;
        OptionRequest optionRequest = new OptionRequest("새 옵션", 5);

        Option expectedOption = new Option();
        expectedOption.setName(optionRequest.getName());
        expectedOption.setQuantity(optionRequest.getQuantity());

        Product product = mock(Product.class);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(optionRepository.findByNameAndProductId(optionRequest.getName(), 1L)).thenReturn(Optional.empty());
        when(optionRepository.save(any(Option.class))).thenReturn(expectedOption);

        Option addedOption = optionService.addOptionToProduct(productId, optionRequest);

        assertAll("옵션 추가 결과",
                () -> assertNotNull(addedOption, "옵션이 null이면 안 됩니다."),
                () -> assertEquals(optionRequest.getName(), addedOption.getName(), "옵션 이름이 맞아야 합니다."),
                () -> assertEquals(optionRequest.getQuantity(), addedOption.getQuantity(), "옵션 수량이 맞아야 합니다.")
        );
    }


    @Test
    public void 옵션삭제_제품이존재하지않는경우() {
        Long productId = 1L;
        Long optionId = 2L;

        when(productRepository.existsById(productId)).thenReturn(false);

        assertThrows(ProductNotFoundException.class,
                () -> optionService.deleteOption(productId, optionId),
                "제품이 존재하지 않아야 합니다.");
    }

    @Test
    public void 옵션삭제_옵션수량이1개일경우() {
        Long productId = 1L;
        Long optionId = 2L;

        when(productRepository.existsById(productId)).thenReturn(true);
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(mock(Option.class)));
        when(optionRepository.countOptionByProductId(productId)).thenReturn(1L);

        assertThrows(MinimumOptionRequiredException.class,
                () -> optionService.deleteOption(productId, optionId),
                "옵션 수량이 1개일 때 삭제하면 안 됩니다.");
    }

    @Test
    public void 옵션삭제_성공() {
        Long productId = 1L;
        Long optionId = 2L;
        Option option1 = mock(Option.class);

        when(productRepository.existsById(productId)).thenReturn(true);
        when(optionRepository.findByIdAndProductId(optionId, productId)).thenReturn(Optional.of(option1));
        when(optionRepository.countOptionByProductId(productId)).thenReturn(2L);

        optionService.deleteOption(productId, optionId);

        verify(optionRepository).delete(option1);
    }

    @Test
    public void 옵션수량감소_성공() {
        Product testProduct = new Product("테스트 상품", 1000, "test.jpg", new Category("테스트 카테고리", "color", "image.url", "description"));
        Option testOption = new Option("옵션1", 10);
        testOption.setProduct(testProduct);

        when(productRepository.existsById(anyLong())).thenReturn(true);
        when(optionRepository.findByIdAndProductId(anyLong(), anyLong())).thenReturn(Optional.of(testOption));

        optionService.subtractOptionQuantity(1L, 1L, 5);

        assertThat(testOption.getQuantity()).isEqualTo(5);
        verify(optionRepository, times(1)).findByIdAndProductId(anyLong(), anyLong());
    }

    @Test
    public void 옵션수량감소_수량부족() {
        Product testProduct = new Product("테스트 상품", 1000, "test.jpg", new Category("테스트 카테고리", "color", "image.url", "description"));
        Option testOption = new Option("옵션1", 10);
        testOption.setProduct(testProduct);

        when(productRepository.existsById(anyLong())).thenReturn(true);
        when(optionRepository.findByIdAndProductId(anyLong(), anyLong())).thenReturn(Optional.of(testOption));

        assertThrows(InsufficientQuantityException.class, () -> {
            optionService.subtractOptionQuantity(1L, 1L, 15);
        });
    }

}
