package gift.wishlist;

import static gift.exception.ErrorMessage.MEMBER_NOT_FOUND;
import static gift.exception.ErrorMessage.PRODUCT_NOT_FOUND;
import static gift.exception.ErrorMessage.WISHLIST_ALREADY_EXISTS;
import static gift.exception.ErrorMessage.WISHLIST_NOT_FOUND;

import gift.member.MemberRepository;
import gift.member.entity.Member;
import gift.product.ProductRepository;
import gift.product.dto.ProductPaginationResponseDTO;
import gift.product.entity.Product;
import gift.token.JwtProvider;
import gift.wishlist.dto.ProductIdRequestDTO;
import gift.wishlist.entity.Wishlist;
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
    private final JwtProvider jwtProvider;

    public WishlistService(
        WishlistRepository wishlistRepository,
        ProductRepository productRepository,
        MemberRepository memberRepository,
        JwtProvider jwtProvider
    ) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Transactional(readOnly = true)
    public Page<ProductPaginationResponseDTO> getAllWishlists(String token, Pageable pageable) {
        return wishlistRepository.findAllByMember(getMemberFromToken(token), pageable)
            .map(wishlist -> {
                Product product = wishlist.getProduct();
                return new ProductPaginationResponseDTO(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl()
                );
            });
    }

    public void addWishlist(String token, ProductIdRequestDTO productIdRequestDTO) {
        Pair<Product, Member> verified = verifyTokenAndProductId(token,
            productIdRequestDTO.getProductId());

        wishlistRepository.findByProductAndMember(verified.getFirst(), verified.getSecond())
            .ifPresent(e -> {
                throw new IllegalArgumentException(WISHLIST_ALREADY_EXISTS);
            });

        wishlistRepository.save(new Wishlist(verified.getFirst(), verified.getSecond()));
    }

    public void deleteWishlist(String token, long productId) {
        Pair<Product, Member> verified = verifyTokenAndProductId(token, productId);

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

    private Pair<Product, Member> verifyTokenAndProductId(String token, long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException(PRODUCT_NOT_FOUND));

        Member member = getMemberFromToken(token);

        return Pair.of(product, member);
    }

    private Member getMemberFromToken(String token) {
        return memberRepository.findById(jwtProvider.getMemberTokenDTOFromToken(token).getEmail())
            .orElseThrow(() -> new IllegalArgumentException(MEMBER_NOT_FOUND));
    }
}
