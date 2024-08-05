package gift.service;

import gift.dto.request.WishRequest;
import gift.dto.response.WishProductResponse;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.WishAlreadyExistsException;
import gift.exception.WishNotFoundException;
import gift.repository.WishRepository;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(WishService.class);

    private final WishRepository wishRepository;
    private final ProductService productService;
    private final MemberService memberService;

    public WishService(WishRepository wishRepository, ProductService productService, MemberService memberService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
        this.memberService = memberService;
    }

    @Transactional
    public void addProductToWish(Long memberId, WishRequest request) {
        Member member = memberService.getMember(memberId);
        Product product = productService.getProduct(request.productId());

        if (wishRepository.existsByMemberAndProduct(member, product)) {
            throw new WishAlreadyExistsException(request.productId());
        }

        Wish wish = new Wish(member, request.quantity(), product);
        wishRepository.save(wish);
    }

    public Page<WishProductResponse> getWishProductResponses(Long memberId, Pageable pageable) {
        Member member = memberService.getMember(memberId);

        return wishRepository.findAllByMember(member, pageable)
                .map(WishProductResponse::fromWish);
    }

    @Transactional
    public void updateWishProductQuantity(Long memberId, WishRequest request) {
        Member member = memberService.getMember(memberId);
        Product product = productService.getProduct(request.productId());

        Wish wish = wishRepository.findByMemberAndProduct(member, product)
                .orElseThrow(WishNotFoundException::new);

        wish.updateQuantity(request.quantity());
    }

    @Transactional
    public void findAndDeleteProductInWish(Long memberId, Long wishId) {
        Wish wish = wishRepository.findByIdAndMemberId(wishId, memberId)
                .orElseThrow(WishNotFoundException::new);

        wishRepository.delete(wish);
    }

    public void deleteProductInWish(Long memberId, Long productId) {
        wishRepository.findByMemberIdAndProductId(memberId, productId)
                .ifPresentOrElse(
                        wishRepository::delete,
                        () -> log.info("위시리스트에 없는 상품입니다")
                );
    }
}
