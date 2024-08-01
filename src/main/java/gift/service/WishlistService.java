package gift.service;

import gift.dto.WishDto;
import gift.exception.CustomNotFoundException;
import gift.model.Product;
import gift.model.Wishlist;
import gift.model.Member;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import gift.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

  private final WishlistRepository wishlistRepository;
  private final ProductRepository productRepository;
  private final MemberRepository memberRepository;

  @Autowired
  public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository, MemberRepository memberRepository) {
    this.wishlistRepository = wishlistRepository;
    this.productRepository = productRepository;
    this.memberRepository = memberRepository;
  }

  public WishDto addWish(WishDto wishDto) {
    Product product = productRepository.findById(wishDto.getProductId())
            .orElseThrow(() -> new CustomNotFoundException("Product not found"));
    Member member = memberRepository.findById(wishDto.getMemberId())
            .orElseThrow(() -> new CustomNotFoundException("Member not found"));
    Wishlist wish = new Wishlist(product, member, wishDto.getOptionId());
    Wishlist savedWish = wishlistRepository.save(wish);
    return new WishDto(savedWish.getId(), savedWish.getProduct().getId(), savedWish.getProduct().getName(), savedWish.getProduct().getPrice(), savedWish.getProduct().getImageUrl(), savedWish.getMember().getId(), savedWish.getOptionId());
  }

  public void deleteWish(Long wishId) {
    Wishlist wish = wishlistRepository.findById(wishId)
            .orElseThrow(() -> new CustomNotFoundException("Wish not found"));
    wishlistRepository.delete(wish);
  }

  public Page<WishDto> getWishes(PageRequest pageRequest) {
    return wishlistRepository.findAll(pageRequest).map(wish ->
            new WishDto(wish.getId(), wish.getProduct().getId(), wish.getProduct().getName(), wish.getProduct().getPrice(), wish.getProduct().getImageUrl(), wish.getMember().getId(), wish.getOptionId()));
  }
}