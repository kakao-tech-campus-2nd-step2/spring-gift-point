package gift.service;

import gift.common.exception.WishNotFoundException;
import gift.entity.Member;
import gift.entity.Product;
import gift.repository.ProductRepository;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.entity.Wish;
import gift.repository.WishRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public WishResponse addWish(Member member, WishRequest request) {
        Product product = validateProductExist(request.getProduct().getId());
        Wish wish = new Wish();
        wish.addWish(member, product);

        wishRepository.save(wish);
        return new WishResponse(wish.getId(), wish.getProduct());
    }

    public List<WishResponse> getWishes(Member member) {
        List<Wish> wishes = wishRepository.findByMemberId(member.getId());
        return wishes.stream()
            .map(wish -> new WishResponse(wish.getId(), wish.getProduct()))
            .collect(Collectors.toList());
    }

    public Slice<Wish> getWishes(Member member, Pageable pageable) {
        return wishRepository.findByMemberId(member.getId(), pageable);
    }

    public void deleteWishByProductId(Member member, Long productId) {
        List<Wish> wishes = wishRepository.findByMemberIdAndProductId(member.getId(), productId);
        if (wishes.isEmpty()) {
            throw new WishNotFoundException(
                "Wishlist에 Product ID: " + productId + "인 상품은 존재하지 않습니다.");
        }
        wishRepository.delete(wishes.get(0));
    }

    public void deleteWishById(Long id) {
        if (!wishRepository.existsById(id)) {
            throw new WishNotFoundException("Wishlist에 ID: " + id + "인 상품은 존재하지 않습니다.");
        }
        wishRepository.deleteById(id);
    }

    private Product validateProductExist(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Product ID: " + productId + "인 상품은 존재하지 않습니다."));
    }

}
