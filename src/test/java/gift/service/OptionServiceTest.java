package gift.service;

import gift.exception.QuantityException;
import gift.exception.DuplicateResourceException;
import gift.product.dto.OptionRequestDto;
import gift.product.dto.OptionResponseDto;
import gift.product.entity.Option;
import gift.product.entity.Product;
import gift.product.repository.OptionRepository;
import gift.product.repository.ProductRepository;
import gift.product.service.OptionService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class OptionServiceTest {

  @Mock
  private OptionRepository optionRepository;

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private OptionService optionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetProductOptions() {
    Product product = new Product();
    Option option = Option.builder()
        .id(1L)
        .name("Option1")
        .quantity(10)
        .product(product)
        .build();
    product.setOptions(Collections.singletonList(option));

    when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

    List<OptionResponseDto> options = optionService.getProductOptions(1L);

    assertEquals(1, options.size());
    OptionResponseDto dto = options.get(0);
    assertEquals(1L, dto.id());
    assertEquals("Option1", dto.name());
    assertEquals(10, dto.quantity());
  }

  @Test
  void testCreateOption() {
    Product product = new Product();
    product.setId(1L);

    OptionRequestDto requestDto = new OptionRequestDto("Option1", 10);

    when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
    when(optionRepository.existsByProductIdAndName(anyLong(), anyString())).thenReturn(false);
    when(optionRepository.save(any(Option.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

    OptionResponseDto responseDto = optionService.createOption(requestDto, 1L);

    assertEquals("Option1", responseDto.name());
    assertEquals(10, responseDto.quantity());
  }

  @Test
  void testCreateOption_DuplicateName() {
    Product product = new Product();
    product.setId(1L);

    OptionRequestDto requestDto = new OptionRequestDto("Option1", 10);

    when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
    when(optionRepository.existsByProductIdAndName(anyLong(), anyString())).thenReturn(true);

    assertThrows(DuplicateResourceException.class, () -> {
      optionService.createOption(requestDto, 1L);
    });
  }

  @Test
  void testUpdateOption() {
    Product product = new Product();
    Option option = Option.builder()
        .id(1L)
        .name("Option1")
        .quantity(10)
        .product(product)
        .build();

    OptionRequestDto requestDto = new OptionRequestDto("Option2", 20);

    when(optionRepository.findByIdAndProductId(anyLong(), anyLong())).thenReturn(Optional.of(option));
    when(optionRepository.existsByProductIdAndName(anyLong(), anyString())).thenReturn(false);
    when(optionRepository.save(any(Option.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

    OptionResponseDto responseDto = optionService.updateOption(requestDto, 1L, 1L);

    assertEquals("Option2", responseDto.name());
    assertEquals(20, responseDto.quantity());
  }

  @Test
  void testUpdateOption_DuplicateName() {
    Product product = new Product();
    Option option = Option.builder()
        .id(1L)
        .name("Option1")
        .quantity(10)
        .product(product)
        .build();

    OptionRequestDto requestDto = new OptionRequestDto("Option2", 20);

    when(optionRepository.findByIdAndProductId(anyLong(), anyLong())).thenReturn(Optional.of(option));
    when(optionRepository.existsByProductIdAndName(anyLong(), anyString())).thenReturn(true);

    assertThrows(DuplicateResourceException.class, () -> {
      optionService.updateOption(requestDto, 1L, 1L);
    });
  }

  @Test
  void testDeleteOption() {
    Option option = Option.builder()
        .id(1L)
        .name("Option1")
        .quantity(10)
        .build();

    when(optionRepository.findByIdAndProductId(anyLong(), anyLong())).thenReturn(Optional.of(option));

    OptionResponseDto responseDto = optionService.deleteOption(1L, 1L);

    assertEquals(1L, responseDto.id());
    assertEquals("Option1", responseDto.name());
    assertEquals(10, responseDto.quantity());

    verify(optionRepository, times(1)).delete(option);
  }

  @Test
  void testSubtractQuantity_Valid() {
    Option option = Option.builder()
        .id(1L)
        .name("Option1")
        .quantity(10)
        .build();

    option.subtractQuantity(5);

    assertEquals(5, option.getQuantity());
  }

  @Test
  void testSubtractQuantity_NegativeQuantity() {
    Option option = Option.builder()
        .id(1L)
        .name("Option1")
        .quantity(10)
        .build();

    assertThrows(QuantityException.class, () -> {
      option.subtractQuantity(0);
    });
  }

  @Test
  void testSubtractQuantity_ExceedsCurrentQuantity() {
    Option option = Option.builder()
        .id(1L)
        .name("Option1")
        .quantity(10)
        .build();

    assertThrows(QuantityException.class, () -> {
      option.subtractQuantity(15);
    });
  }
}
