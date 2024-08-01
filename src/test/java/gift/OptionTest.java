package gift;

import gift.dto.OptionDto;
import gift.exception.CustomNotFoundException;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.service.OptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OptionTest {

  private OptionRepository optionRepository;
  private ProductRepository productRepository;
  private OptionService optionService;

  @BeforeEach
  void setUp() {
    optionRepository = mock(OptionRepository.class);
    productRepository = mock(ProductRepository.class);
    optionService = new OptionService(optionRepository, productRepository);
  }

  @Test
  void testOptionCreation() {
    Product product = new Product("Test Product", 1000, "test.png", null);
    Option option = new Option("Option 1", 10, product);

    assertNotNull(option);
    assertEquals("Option 1", option.getName());
    assertEquals(10, option.getQuantity());
    assertEquals(product, option.getProduct());
  }

  @Test
  void testOptionCreationWithInvalidName() {
    Product product = new Product("Test Product", 1000, "test.png", null);

    assertThrows(IllegalArgumentException.class, () -> new Option("", 10, product));
    assertThrows(IllegalArgumentException.class, () -> new Option("test".repeat(50), 10, product));
    assertThrows(IllegalArgumentException.class, () -> new Option("Invalid@Name", 10, product));
  }

  @Test
  void testOptionCreationWithInvalidQuantity() {
    Product product = new Product("Test Product", 1000, "test.png", null);

    assertThrows(IllegalArgumentException.class, () -> new Option("Option 1", 0, product));
    assertThrows(IllegalArgumentException.class, () -> new Option("Option 1", 100_000_000, product));
  }

  @Test
  void testOptionDelete() {
    Product product = new Product("Test Product", 1000, "test.png", null);
    Option option = new Option("Option 1", 10, product);

    product.removeOption(option);
    assertNull(option.getProduct());
  }

  @Test
  void testOptionAdd() {
    Product product = new Product("Test Product", 1000, "test.png", null);
    Option option = new Option("Option 1", 10, product);
    Product product1 = new Product("Test Product1", 1000, "test.png", null);
    Option option1 = new Option("Option 2", 10, product1);

    assertNotNull(option);
    assertEquals("Option 1", option.getName());
    assertEquals(10, option.getQuantity());
    assertEquals(product, option.getProduct());

    assertNotNull(option1);
    assertEquals("Option 2", option1.getName());
    assertEquals(10, option1.getQuantity());
    assertEquals(product1, option1.getProduct());
  }

  @Test
  void testAddOptionToProduct() {
    Long productId = 1L;
    OptionDto optionDto = new OptionDto(null, "Option Name", 10);
    Product product = new Product("Test Product", 1000, "test.png", null);
    Option option = new Option(optionDto.getName(), optionDto.getQuantity(), product);

    when(productRepository.findById(productId)).thenReturn(Optional.of(product));
    when(optionRepository.save(any(Option.class))).thenReturn(option);

    OptionDto createdOption = optionService.addOptionToProduct(productId, optionDto);

    assertNotNull(createdOption);
    assertEquals(optionDto.getName(), createdOption.getName());
    assertEquals(optionDto.getQuantity(), createdOption.getQuantity());

    verify(optionRepository, times(1)).save(any(Option.class));
  }

  @Test
  void testAddOptionToProduct_ProductNotFound() {
    Long productId = 1L;
    OptionDto optionDto = new OptionDto(null, "Option Name", 10);

    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    assertThrows(CustomNotFoundException.class, () -> optionService.addOptionToProduct(productId, optionDto));
  }
  @Test
  void subtractQuantity_WithSufficientStock() {
    Option option = new Option("Option Name", 10, null);
    option.subtractQuantity(5);
    assertEquals(5, option.getQuantity());
  }

  @Test
  void subtractQuantity_WithInsufficientStock() {
    Option option = new Option("Option Name", 5, null);
    assertThrows(IllegalArgumentException.class, () -> option.subtractQuantity(10));
  }

  @Test
  void  invalidNameTest(){
    assertThrows(IllegalArgumentException.class, () ->  new Option("",10, null));
    assertThrows(IllegalArgumentException.class, () ->  new Option("Option@",10, null));
    assertThrows(IllegalArgumentException.class, () ->  new Option("123".repeat(50),10, null));
  }

}
