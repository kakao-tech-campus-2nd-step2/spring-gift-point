package gift.wish.service;

import gift.member.model.Member;
import gift.product.model.Product;
import gift.wish.model.Wish;
import gift.product.repository.ProductRepository;
import gift.wish.repository.WishRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        member.getWishList().add(wish);
        return wishRepository.save(wish);
    }

    @Transactional
    public boolean removeWish(Long id, Member member) {
        return wishRepository.deleteByIdAndMember(id, member) > 0;
    }

    public Page<Wish> getWishListPage(Member member,
        int page, int pageSize, String sortProperty,
        Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, page, Sort.by(new Order(direction, sortProperty)));
        return wishRepository.findAllByMember(pageable, member);
    }

}
