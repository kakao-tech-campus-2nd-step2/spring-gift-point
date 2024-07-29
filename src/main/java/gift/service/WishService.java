package gift.service;

import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public List<Wish> getWishesByMember(Member member) {
        return wishRepository.findByMember(member);
    }

    public List<Wish> getWishesByProductId(Product product) {
        return wishRepository.findByProductId(product.getId());
    }

    public Wish addWish(Long productId, Member member) {
        var product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("없는 상품입니다."));
        var wish = new Wish(product, member);
        return wishRepository.save(wish);
    }

    public boolean removeWish(Long id, Member member) {
        return wishRepository.deleteByIdAndMember(id, member) > 0;
    }
}
