package gift.wish.service;

import gift.member.domain.Member;
import gift.member.persistence.MemberRepository;
import gift.product.domain.Product;
import gift.product.exception.product.ProductNotFoundException;
import gift.product.persistence.ProductRepository;
import gift.wish.domain.Wish;
import gift.wish.exception.WishNotFoundException;
import gift.wish.persistence.WishRepository;
import gift.wish.service.command.WishCommand;
import gift.wish.service.dto.WishInfo;
import gift.wish.service.dto.WishPageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishService(WishRepository wishRepository,
                       ProductRepository productRepository,
                       MemberRepository memberRepository
    ) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public Long saveWish(WishCommand wishRequest) {
        Product product = productRepository.findById(wishRequest.productId())
                .orElseThrow(() -> ProductNotFoundException.of(wishRequest.productId()));
        Member member = memberRepository.getReferenceById(wishRequest.memberId());

        Wish wish = new Wish(wishRequest.amount(), product, member);
        wishRepository.save(wish);
        return wish.getId();
    }

    @Transactional
    public void updateWish(WishCommand wishRequest, final Long wishId) {
        Wish wish = wishRepository.findWishByIdWithUserAndProduct(wishId)
                .orElseThrow(() -> WishNotFoundException.of(wishId));

        Member member = memberRepository.getReferenceById(wishRequest.memberId());
        Product product = productRepository.getReferenceById(wishRequest.productId());

        wish.modify(wishRequest.amount(), product, member);
    }

    @Transactional(readOnly = true)
    public WishPageInfo getWishList(final Long userId, final Pageable pageable) {
        Page<Wish> wishes = wishRepository.findWishesByUserIdWithMemberAndProduct(userId, pageable);

        return WishPageInfo.from(wishes);
    }

    @Transactional(readOnly = true)
    public WishInfo getWish(final Long wishId, final Long userId) {
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> WishNotFoundException.of(wishId));
        if (!wish.isOwner(userId)) {
            throw WishNotFoundException.of(wishId);
        }

        if (wish.beDeleted()) {
            deleteWish(wishId, userId);
        }
        return WishInfo.from(wish);
    }

    @Transactional
    public void deleteWish(final Long wishId, final Long userId) {
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> WishNotFoundException.of(wishId));
        if (!wish.isOwner(userId)) {
            throw WishNotFoundException.of(wishId);
        }

        wishRepository.delete(wish);
    }

    @Transactional
    public void deleteWishWithPurchase(final Long productId, final Long memberId) {
        wishRepository.findWishByMemberIdAndProductId(memberId, productId)
                .ifPresent(wish -> wishRepository.deleteById(wish.getId()));
    }
}
