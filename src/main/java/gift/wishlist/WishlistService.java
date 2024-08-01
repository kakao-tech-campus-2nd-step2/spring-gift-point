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

//    public Page<Product> checkWishlist(Pageable pageable) {
//        Page<Wishlist> wishlistPage = wishlistRepository.findAll(pageable);
//        return wishlistPage.map(Wishlist::getProduct);
//    }

    public Page<Wishlist> checkWishlist(Pageable pageable) {
        return wishlistRepository.findAll(pageable);
    }

    @Transactional
    public WishResponseDto addWishlist(WishRequestDto request, Member member) {
        //Optional<Product> product = productRepository.findById(request.productId());

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new InvalidProduct("유효하지 않은 상품입니다"));

        Wishlist wishlist = new Wishlist(member, product, request.quantity());

        wishlistRepository.saveAndFlush(wishlist);

        return new WishResponseDto(wishlist.getId(), wishlist.getQuantity());
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
