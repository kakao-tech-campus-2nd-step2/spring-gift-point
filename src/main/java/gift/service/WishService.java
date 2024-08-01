package gift.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.WishDTO;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public Page<WishDTO> getWishes(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Page<Wish> wishes = wishRepository.findByMember(member, pageable);

        return wishes.map(wish -> new WishDTO(
                wish.getId(),
                wish.getProduct().getId(),
                wish.getProduct().getName(),
                wish.getCreatedDate()
        ));
    }

    @Transactional
    public void addWish(String email, Long productId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (!wishRepository.existsByMemberAndProduct(member, product)) {
            Wish wish = new Wish(member, product);
            wishRepository.save(wish);
        }
    }

    @Transactional
    public void removeWish(String email, Long productId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        wishRepository.deleteByMemberAndProduct(member, product);
    }

    public WishDTO convertToDTO(Wish wish) {
        return new WishDTO(
                wish.getId(),
                wish.getProduct().getId(),
                wish.getProduct().getName(),
                wish.getCreatedDate()
        );
    }
}
