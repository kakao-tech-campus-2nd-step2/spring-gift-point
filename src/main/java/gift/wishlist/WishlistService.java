package gift.wishlist;

import gift.exception.InvalidProduct;
import gift.member.Member;
import gift.member.MemberRepository;
import gift.product.Product;
import gift.product.ProductRepository;
import gift.product.ProductResponseDto;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishlistService(
        WishlistRepository wishlistRepository,
        ProductRepository productRepository,
        MemberRepository memberRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public Page<Product> checkWishlist(Pageable pageable) {
        Page<Wishlist> wishlistPage = wishlistRepository.findAll(pageable);
        return wishlistPage.map(Wishlist::getProduct);
    }

    @Transactional
    public void addWishlist(WishRequestDto request, Member member) {
        Optional<Product> product = productRepository.findById(request.productId());

        wishlistRepository.saveAndFlush(new Wishlist(member, product.get()));
    }

    @Transactional
    public void deleteWishlist(Long productId, Long memberId) {
        List<Wishlist> wishlist1 = wishlistRepository.findByProductId(productId);
        List<Wishlist> wishlist2 = wishlistRepository.findByMemberId(memberId);

        Wishlist wishlistItem = null;
        for (Wishlist item : wishlist1) {
            if (wishlist2.contains(item)) {
                wishlistItem = item;
                break;
            }
        }

        if (wishlistItem != null) {
            wishlistRepository.delete(wishlistItem);
        } else {
            throw new InvalidProduct("잘못된 접근입니다");
        }
    }
}
