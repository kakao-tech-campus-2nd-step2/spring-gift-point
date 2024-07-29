package gift.wish.service;

import static gift.wish.dto.WishDto.toDto;

import gift.exception.BadRequestException;
import gift.exception.ResourceNotFoundException;

import gift.product.dto.ProductDto;
import gift.user.dto.UserDto;
import gift.product.entity.Product;
import gift.user.entity.User;
import gift.wish.dto.WishDto;
import gift.wish.entity.Wish;
import gift.user.repository.UserRepository;
import gift.product.repository.ProductRepository;
import gift.wish.repository.WishRepository;
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

  public Page<WishDto> getWishesByMemberEmail(String userEmail, Pageable pageable) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));
    Page<Wish> wishes = wishRepository.findByUserId(user.getId(), pageable);
    return wishes.map(WishDto::toDto);
  }

  public WishDto addWish(WishDto wishDto) {
    User user = userRepository.findById(wishDto.getUser().getId())
        .orElseThrow(() -> new ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));
    Product product = productRepository.findById(wishDto.getProduct().getId())
        .orElseThrow(() -> new ResourceNotFoundException("상품 정보를 찾을 수 없습니다."));

    if (wishRepository.findByUserIdAndProduct(user.getId(), product).isPresent()) {
      throw new BadRequestException("중복된 위시리스트 항목입니다.");
    }

    Wish wish = new Wish();
    wish.setUser(user);
    wish.setProduct(product);
    Wish savedWish = wishRepository.save(wish);

    return toDto(savedWish);

  }

  public void removeWish(String memberEmail, Long productId) {
    User user = userRepository.findByEmail(memberEmail)
        .orElseThrow(() -> new ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("상품 정보를 찾을 수 없습니다."));

    wishRepository.deleteByUserIdAndProduct(user.getId(), product);
  }
}
