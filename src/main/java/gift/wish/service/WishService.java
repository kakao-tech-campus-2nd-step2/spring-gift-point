package gift.wish.service;

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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class WishService {

  private final WishRepository wishRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  @Autowired
  public WishService(WishRepository wishRepository, UserRepository userRepository,
      ProductRepository productRepository) {
    this.wishRepository = wishRepository;
    this.userRepository = userRepository;
    this.productRepository = productRepository;
  }

  public Page<WishResponseDto> getWishesByUserEmail(String email, Pageable pageable) {
    return wishRepository.findByUserEmail(email, pageable)
        .map(WishResponseDto::toDto);
  }


  public WishResponseDto addWish(Long userId,@Valid WishRequestDto wishRequestDto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));
    Product product = productRepository.findById(wishRequestDto.productId())
        .orElseThrow(() -> new ResourceNotFoundException("상품 정보를 찾을 수 없습니다."));

    if (wishRepository.findByUserIdAndProduct(user.getId(), product).isPresent()) {
      throw new BadRequestException("중복된 위시리스트 항목입니다.");
    }

    Wish wish = Wish.builder()
        .user(user)
        .product(product)
        .count(wishRequestDto.count())
        .build();

    Wish savedWish = wishRepository.save(wish);
    return WishResponseDto.toDto(savedWish);
  }

  public WishResponseDto removeWish(Long userId, Long wishId) {
    Wish wish = wishRepository.findByIdAndUserId(wishId, userId)
        .orElseThrow(() -> new ResourceNotFoundException("Wish item not found"));

    wishRepository.delete(wish);
    return WishResponseDto.toDto(wish);
  }
}
