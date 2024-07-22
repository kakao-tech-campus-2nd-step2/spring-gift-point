package gift.service;

import static gift.util.constants.WishConstants.ALREADY_EXISTS;
import static gift.util.constants.WishConstants.PERMISSION_DENIED;
import static gift.util.constants.WishConstants.WISH_NOT_FOUND;

import gift.dto.member.MemberResponse;
import gift.dto.product.ProductResponse;
import gift.dto.wish.WishCreateRequest;
import gift.dto.wish.WishResponse;
import gift.exception.wish.DuplicateWishException;
import gift.exception.wish.PermissionDeniedException;
import gift.exception.wish.WishNotFoundException;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductService productService;
    private final MemberService memberService;

    public WishService(
        WishRepository wishRepository,
        ProductService productService,
        MemberService memberService
    ) {
        this.wishRepository = wishRepository;
        this.productService = productService;
        this.memberService = memberService;
    }

    public Page<WishResponse> getWishlistByMemberId(Long memberId, Pageable pageable) {
        return wishRepository.findAllByMember_Id(memberId, pageable).map(this::convertToDTO);
    }

    public WishResponse addWish(WishCreateRequest wishCreateRequest, Long memberId) {
        MemberResponse memberResponse = memberService.getMemberById(memberId);
        Member member = memberService.convertToEntity(memberResponse);

        ProductResponse productResponse = productService.getProductById(
            wishCreateRequest.productId()
        );
        Product product = productService.convertToEntity(productResponse);

        if (wishRepository.existsByMember_IdAndProduct_Id(member.getId(), product.getId())) {
            throw new DuplicateWishException(ALREADY_EXISTS);
        }

        Wish wish = new Wish(member, product);
        Wish savedWish = wishRepository.save(wish);
        return convertToDTO(savedWish);
    }

    public void deleteWish(Long wishId, Long memberId) {
        Wish wish = wishRepository.findById(wishId)
            .orElseThrow(() -> new WishNotFoundException(WISH_NOT_FOUND + wishId));

        if (!wish.isOwnedBy(memberId)) {
            throw new PermissionDeniedException(PERMISSION_DENIED);
        }

        wishRepository.delete(wish);
    }

    // Mapper methods
    private WishResponse convertToDTO(Wish wish) {
        return new WishResponse(wish.getId(), wish.getMemberId(), wish.getProductId());
    }
}
