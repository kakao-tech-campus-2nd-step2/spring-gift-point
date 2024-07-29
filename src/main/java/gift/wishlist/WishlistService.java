package gift.wishlist;

import static gift.exception.ErrorMessage.MEMBER_NOT_FOUND;
import static gift.exception.ErrorMessage.PRODUCT_NOT_FOUND;
import static gift.exception.ErrorMessage.WISHLIST_ALREADY_EXISTS;
import static gift.exception.ErrorMessage.WISHLIST_NOT_FOUND;

import gift.member.Member;
import gift.member.MemberRepository;
import gift.product.Product;
import gift.product.ProductRepository;
import gift.token.MemberTokenDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishlistService(
        WishlistRepository wishlistRepository,
        ProductRepository productRepository,
        MemberRepository memberRepository
    ) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public Page<Product> getAllWishlists(MemberTokenDTO memberTokenDTO, Pageable pageable) {
        Member member = memberTokenDTOToMember(memberTokenDTO);

        return wishlistRepository
            .findAllByMember(member, pageable)
            .map(Wishlist::getProduct);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllWishlists(MemberTokenDTO memberTokenDTO) {
        Member member = memberTokenDTOToMember(memberTokenDTO);

        return wishlistRepository.findAllByMember(member)
            .stream()
            .map(Wishlist::getProduct)
            .toList();
    }

    public void addWishlist(MemberTokenDTO memberTokenDTO, long productId) {
        Pair<Product, Member> verified = verifyTokenAndProductId(memberTokenDTO, productId);

        wishlistRepository.findByProductAndMember(verified.getFirst(), verified.getSecond())
            .ifPresent(e -> {
                throw new IllegalArgumentException(WISHLIST_ALREADY_EXISTS);
            });

        wishlistRepository.save(new Wishlist(verified.getFirst(), verified.getSecond()));
    }

    public void deleteWishlist(MemberTokenDTO memberTokenDTO, long productId) {
        Pair<Product, Member> verified = verifyTokenAndProductId(memberTokenDTO, productId);

        Wishlist findWishlist = wishlistRepository
            .findByProductAndMember(verified.getFirst(), verified.getSecond())
            .orElseThrow(() -> new IllegalArgumentException(WISHLIST_NOT_FOUND));

        wishlistRepository.delete(findWishlist);
    }

    public void deleteWishlistIfExists(Product product, Member member) {
        wishlistRepository.findByProductAndMember(
            product, member
        ).ifPresent(wishlistRepository::delete);
    }

    private Pair<Product, Member> verifyTokenAndProductId(
        MemberTokenDTO memberTokenDTO,
        long productId
    ) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException(PRODUCT_NOT_FOUND));

        Member member = memberTokenDTOToMember(memberTokenDTO);

        return Pair.of(product, member);
    }

    private Member memberTokenDTOToMember(MemberTokenDTO memberTokenDTO) {
        return memberRepository.findById(memberTokenDTO.getEmail())
            .orElseThrow(() -> new IllegalArgumentException(MEMBER_NOT_FOUND));
    }
}
