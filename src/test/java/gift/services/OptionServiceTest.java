//package gift.services;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import gift.classes.Exceptions.OptionException;
//import gift.domain.Category;
//import gift.domain.Option;
//import gift.domain.Product;
//import gift.dto.OptionDto;
//import gift.dto.RequestOptionDto;
//import gift.repositories.OptionRepository;
//import gift.repositories.ProductRepository;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class OptionServiceTest {
//
//    @Mock
//    private OptionRepository optionRepository;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @InjectMocks
//    private OptionService optionService;
//
//    @Test
//    @DisplayName("특정 상품의 옵션 조회 테스트")
//    void testGetOptionsByProductId() {
//        Long productId = 1L;
//        Category category = new Category(1L, "Test Category", "Blue", "CategoryImageUrl", "Test");
//        Product product = new Product("Test Product", 300, "ImageUrl", category);
//        product.setId(productId);
//
//        Option option1 = new Option("Test Option1", 10, product);
//        Option option2 = new Option("Test Option2", 5, product);
//
//        List<Option> options = List.of(option1, option2);
//
//        when(optionRepository.findAllByProductId(productId)).thenReturn(options);
//
//        List<OptionDto> result = optionService.getOptionsByProductId(productId);
//
//        assertEquals(2, result.size());
//        assertEquals("Test Option1", result.get(0).getName());
//        assertEquals(10, result.get(0).getQuantity());
//    }
//
//    @Test
//    @DisplayName("옵션 저장 테스트")
//    void testAddOption() {
//        Long productId = 1L;
//        Category category = new Category(1L, "Test Category", "Blue", "CategoryImageUrl", "Test");
//        Product product = new Product("Test Product", 300, "ImageUrl", category);
//        product.setId(productId);
//
//        RequestOptionDto requestOptionDto = new RequestOptionDto("Test Option", 15);
//        Option savedOption = new Option(1L, requestOptionDto.getName(), requestOptionDto.getQuantity(), product);
//
//        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
//        when(optionRepository.findAllByProductId(productId)).thenReturn(new ArrayList<>());
//        when(optionRepository.save(any(Option.class))).thenReturn(savedOption);
//
//        optionService.addOption(productId, requestOptionDto);
//
//        verify(optionRepository).save(any(Option.class));
//    }
//
//    @Test
//    @DisplayName("옵션 수정 테스트")
//    void testUpdateOption() {
//        Long optionId = 1L;
//        RequestOptionDto requestOptionDto = new RequestOptionDto("Updated Option", 20);
//        Option existingOption = new Option("Old Option", 10,
//            new Product(null, 0, "ImageUrl", null));
//
//        when(optionRepository.findById(optionId)).thenReturn(Optional.of(existingOption));
//
//        optionService.updateOption(optionId, requestOptionDto);
//
//        assertEquals("Updated Option", existingOption.getName());
//        assertEquals(20, existingOption.getQuantity());
//    }
//
//    @Test
//    @DisplayName("옵션 삭제 테스트")
//    void testDeleteOption() {
//        Long optionId = 1L;
//        Option existingOption = new Option("Option to Delete", 10,
//            new Product(null, 0, "ImageUrl", null));
//
//        when(optionRepository.findById(optionId)).thenReturn(Optional.of(existingOption));
//
//        optionService.deleteOption(optionId);
//
//        verify(optionRepository).delete(existingOption);
//    }
//
//    @Test
//    @DisplayName("옵션 수량 차감 테스트")
//    void testDeductOption() {
//        Long optionId = 1L;
//        Option existingOption = new Option("Option", 10, new Product(null, 0, "ImageUrl", null));
//
//        when(optionRepository.findById(optionId)).thenReturn(Optional.of(existingOption));
//
//        optionService.deductOption(optionId, 5);
//
//        assertEquals(5, existingOption.getQuantity());
//    }
//
//    @Test
//    @DisplayName("옵션 수량 차감시 수량 부족 예외 테스트")
//    void testDeductOption_WhenInsufficientAmount() {
//        Long optionId = 1L;
//        Option existingOption = new Option("Option", 3, new Product(null, 0, "ImageUrl", null));
//
//        when(optionRepository.findById(optionId)).thenReturn(Optional.of(existingOption));
//
//        Exception exception = assertThrows(OptionException.class, () -> {
//            optionService.deductOption(optionId, 5);
//        });
//
//        assertEquals("Deduction failed due to insufficient quantity.", exception.getMessage());
//    }
//}