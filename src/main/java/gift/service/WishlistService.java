package gift.service;

import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    public void addProduct(Long memberId, WishRequest wishRequest) {
        Product product = productRepository.findById(wishRequest.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + wishRequest.productId() + " not found"));

        Optional<Wish> existingWish = wishlistRepository.findByMemberIdAndProductId(memberId, product.getId());
        if (existingWish.isPresent()) {
            return;
        }

        Member member = new Member();
        member.setId(memberId);

        Wish wish = new Wish(member, product);
        wishlistRepository.save(wish);
    }

    public Page<WishResponse> getWishesByMemberId(Long memberId, PageRequest pageRequest) {
        return wishlistRepository.findByMemberId(memberId, pageRequest)
                .map(wish -> new WishResponse(wish.getId(), wish.getProduct().getId(), wish.getProduct().getName(), wish.getProduct().getImageUrl()));
    }

    public void deleteItem(Long wishId) {
        if (!wishlistRepository.existsById(wishId)) {
            throw new IllegalArgumentException("Wish with id " + wishId + " not found");
        }
        wishlistRepository.deleteById(wishId);
    }
}
