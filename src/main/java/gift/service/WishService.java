package gift.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Page<Wish> getWishes(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return wishRepository.findByMember(member, pageable);
    }

    public void addWish(Long memberId, String productName) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (!wishRepository.existsByMemberAndProduct(member, product)) {
            Wish wish = new Wish(member, product);
            wishRepository.save(wish);
        }
    }

    public void removeWish(Long memberId, String productName) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        wishRepository.deleteByMemberAndProduct(member, product);
    }
}
