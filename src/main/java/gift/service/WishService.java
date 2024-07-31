package gift.service;

import gift.common.exception.conflict.WishAlreadyExistsException;
import gift.common.exception.notFound.ProductNotFoundException;
import gift.common.exception.notFound.WishNotFoundException;
import gift.entity.Member;
import gift.entity.Product;
import gift.repository.ProductRepository;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.entity.Wish;
import gift.repository.WishRepository;
import jakarta.transaction.Transactional;
import java.util.List;
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
        Product product = validateProductExist(request.getProductId());

        if (wishRepository.findByMemberIdAndProductId(member.getId(), product.getId())
            .isPresent()) {
            throw new WishAlreadyExistsException();
        }

        Wish wish = new Wish();
        wish.addWish(member, product);
        wishRepository.save(wish);
        return new WishResponse(wish.getId(), wish.getProduct());
    }

    public List<Wish> getWishes(Member member) {
        return wishRepository.findByMemberId(member.getId());
    }

    public void deleteWishById(Member member, Long id) {
        if (!wishRepository.existsById(id)) {
            throw new WishNotFoundException();
        }
        wishRepository.deleteById(id);
    }

    private Product validateProductExist(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);
    }

}
