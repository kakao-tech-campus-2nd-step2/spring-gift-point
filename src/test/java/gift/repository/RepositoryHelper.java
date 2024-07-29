package gift.repository;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RepositoryHelper {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final WishRepository wishRepository;

    public RepositoryHelper(ProductRepository productRepository, MemberRepository memberRepository, WishRepository wishRepository) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.wishRepository = wishRepository;
    }

    public Optional<Product> findProductById(Long productId) {
        return productRepository.findById(productId);
    }

    public Optional<Member> findMemberById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public Optional<Wish> findWishById(Long wishId) {
        return wishRepository.findById(wishId);
    }
}
