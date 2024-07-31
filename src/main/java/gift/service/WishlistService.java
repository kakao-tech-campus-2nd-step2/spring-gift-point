package gift.service;

import gift.model.Member;
import gift.model.Product;
import gift.model.Wishlist;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WishlistService {

  private final WishlistRepository wishlistRepository;
  private final MemberService memberService;
  private final ProductService productService;
  private final ProductRepository productRepository;

  @Autowired
  public WishlistService(WishlistRepository wishlistRepository, MemberService memberService, ProductService productService, ProductRepository productRepository) {
    this.wishlistRepository = wishlistRepository;
    this.memberService = memberService;
    this.productService = productService;
    this.productRepository = productRepository;
  }


  public Wishlist addWishlistItem(Member member, Map<String, Object> body) {
    Map<String, Object> product = (Map<String, Object>) body.get("product");
    Object idObject = product.get("id");
    Long productId;
    if (idObject instanceof Integer) {
      productId = Long.valueOf((Integer) idObject);
    } else {
      productId = (Long) idObject;
    }
    Product productEntity = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productId));
    Wishlist wishlist = new Wishlist();
    wishlist.setMember(member);
    wishlist.setProduct(productEntity);
    return wishlistRepository.save(wishlist);
  }

  public Page<Wishlist> getWishlist(Long memberId, int page, int size) {
    if (memberId == null) {
      throw new IllegalArgumentException("Member ID cannot be null");
    }
    if (page < 0) {
      throw new IllegalArgumentException("Page index must not be less than zero");
    }
    if (size < 1) {
      throw new IllegalArgumentException("Page size must not be less than one");
    }

    Pageable pageable = PageRequest.of(page, size);
    return wishlistRepository.findByMemberId(memberId, pageable);
  }

  public void removeWishlistItem(Long id) {
    wishlistRepository.deleteById(id);
  }
}
