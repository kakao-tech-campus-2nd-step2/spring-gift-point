package gift.service;

import gift.exception.BadRequestException;
import gift.exception.ResourceNotFoundException;
import gift.product.entity.Product;
import gift.user.entity.User;
import gift.wish.dto.WishRequestDto;
import gift.wish.dto.WishResponseDto;
import gift.wish.entity.Wish;
import gift.user.repository.UserRepository;
import gift.product.repository.ProductRepository;
import gift.wish.repository.WishRepository;
import gift.wish.service.WishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class WishServiceTest {

  @Mock
  private WishRepository wishRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private WishService wishService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetWishesByUserEmail() {
    User user = new User();
    user.setEmail("test@example.com");

    Wish wish = Wish.builder()
        .id(1L)
        .user(user)
        .product(new Product())
        .count(2)
        .build();

    Page<Wish> wishPage = new PageImpl<>(Collections.singletonList(wish));
    when(wishRepository.findByUserEmail(anyString(), any(Pageable.class))).thenReturn(wishPage);

    Page<WishResponseDto> responseDtos = wishService.getWishesByUserEmail("test@example.com", Pageable.unpaged());

    assertEquals(1, responseDtos.getTotalElements());
    WishResponseDto dto = responseDtos.getContent().get(0);
    assertEquals(1L, dto.id());
    assertEquals(2, dto.count());
  }

  @Test
  void testAddWish() {
    User user = new User();
    user.setId(1L);

    Product product = new Product();
    product.setId(1L);

    WishRequestDto requestDto = new WishRequestDto(1L, 3);

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
    when(wishRepository.findByUserIdAndProduct(anyLong(), any(Product.class))).thenReturn(Optional.empty());
    when(wishRepository.save(any(Wish.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

    WishResponseDto responseDto = wishService.addWish(1L, requestDto);

    assertEquals(3, responseDto.count());
  }

  @Test
  void testAddWish_DuplicateWish() {
    User user = new User();
    user.setId(1L);

    Product product = new Product();
    product.setId(1L);

    WishRequestDto requestDto = new WishRequestDto(1L, 3);

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
    when(wishRepository.findByUserIdAndProduct(anyLong(), any(Product.class)))
        .thenReturn(Optional.of(new Wish()));

    assertThrows(BadRequestException.class, () -> {
      wishService.addWish(1L, requestDto);
    });
  }

  @Test
  void testRemoveWish() {
    Wish wish = Wish.builder()
        .id(1L)
        .user(new User())
        .product(new Product())
        .count(2)
        .build();

    when(wishRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(wish));

    WishResponseDto responseDto = wishService.removeWish(1L, 1L);

    assertEquals(1L, responseDto.id());
    assertEquals(2, responseDto.count());

    verify(wishRepository, times(1)).delete(wish);
  }

  @Test
  void testRemoveWish_NotFound() {
    when(wishRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      wishService.removeWish(1L, 1L);
    });
  }
}
