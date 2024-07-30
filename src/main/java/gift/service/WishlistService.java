package gift.service;

import gift.model.Member;
import gift.model.Product;
import gift.model.Wishlist;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, MemberRepository memberRepository,
                           ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Page<Wishlist> getWishlist(Long memberId, Pageable pageable) {
        return wishlistRepository.findByMemberId(memberId, pageable);
    }

    public void addProductToWishlist(Long memberId, Long productId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        Wishlist wishlist = new Wishlist(member, product);
        wishlistRepository.save(wishlist);
    }

    @Transactional
    public void removeProductFromWishlist(Long memberId, Long productId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        wishlistRepository.deleteByMemberIdAndProductId(memberId, productId);
    }
}
