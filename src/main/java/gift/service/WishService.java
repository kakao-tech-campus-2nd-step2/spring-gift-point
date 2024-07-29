package gift.service;

import gift.dto.WishRequest;
import gift.exception.NotFoundException;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public Wish makeWish(WishRequest request, Member member) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new NotFoundException("해당 productId의 상품을 찾을 수 없습니다."));
        Wish wish = new Wish(product, member);
        wishRepository.save(wish);
        return wish;
    }

    public Page<Product> getAllWishProductsByMember(Member member, Pageable pageable) {
        return wishRepository.findAllByMemberId(member.getId(), pageable);
    }

    @Transactional
    public void deleteWish(Long productId, Member member) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 productId의 상품을 찾을 수 없습니다."));
        wishRepository.deleteByProductAndMember(product, member);
    }
}
