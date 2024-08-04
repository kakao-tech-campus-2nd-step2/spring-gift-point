package gift.service;

import gift.repository.WishlistRepository;
import gift.vo.Member;
import gift.vo.Product;
import gift.vo.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductService productService;

    public WishlistService(WishlistRepository wishlistRepository, ProductService productService) {
        this.wishlistRepository = wishlistRepository;
        this.productService = productService;
    }

    private Product getProduct(Long productId) {
        return productService.getProductById(productId);
    }

    public Page<Wish> getWishProductList(Member member, Pageable pageable) {
        return wishlistRepository.findByMemberId(member.getId(), pageable);
    }

    public void addWishProduct(Member member, Long productId) {
        Wish wish = new Wish(member, getProduct(productId));
        wishlistRepository.save(wish);
    }

    public void deleteWishProduct(Long id) {
        wishlistRepository.deleteById(id);
    }

    public Long hasFindWishByMemberAndProduct(Member member, Product product) {
        return wishlistRepository.findByMemberAndProduct(member, product)
                .map(Wish::getId)
                .orElse(null);
    }
}
