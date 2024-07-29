package gift.service;

import gift.model.Member;
import gift.model.WishList;
import gift.repository.MemberRepository;
import gift.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository, MemberRepository memberRepository) {
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
    }

    public WishList addProduct(Long memberId, Long productId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        WishList wishlist = new WishList(member, productId);
        WishList savedWishlist = wishlistRepository.save(wishlist);
        return savedWishlist;

    }

    public Page<WishList> getProductsByMember(Long memberId, int page, int size) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Pageable pageable = PageRequest.of(page, size);
        return wishlistRepository.findByMember(member, pageable);
    }

    public void deleteById(Long productId) {
        wishlistRepository.deleteById(productId);
    }
}
