package gift.service;

import gift.domain.member.Member;
import gift.domain.member.MemberRepository;
import gift.domain.product.Product;
import gift.domain.product.ProductRepository;
import gift.domain.wish.Wish;
import gift.domain.wish.WishRepository;
import gift.dto.WishAddRequestDto;
import gift.dto.WishResponseDto;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WishService {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final WishRepository wishRepository;

    public WishService(MemberRepository memberRepository, ProductRepository productRepository, WishRepository wishRepository) {
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.wishRepository = wishRepository;
    }

    @Transactional
    public void addWish(Long memberId, WishAddRequestDto request) {
        Member member = getMember(memberId);
        wishRepository.findByMemberIdAndProductId(memberId, request.getProductId())
                .ifPresentOrElse(
                        wish -> wish.addQuantity(request.getQuantity()),
                        () -> {
                            Wish newWish = new Wish(member, getProduct(request.getProductId()), request.getQuantity());
                            wishRepository.save(newWish);
                        }
                );
    }

    public List<WishResponseDto> getAllWishes(Long id, Pageable pageable) {
        List<Wish> wishList = wishRepository.findAllByMemberId(id, pageable);
        return wishList.stream().map(WishResponseDto::new).toList();
    }

    public void updateWish(Long memberId, Long wishId, int quantity) {
        checkMemberValidation(memberId);
        Wish wish = getWish(wishId);
        if (quantity == 0) {
            wishRepository.delete(wish);
            return;
        }
        wish.updateQuantity(quantity);
        wishRepository.save(wish);
    }

    @Transactional
    public void deleteWish(Long memberId, Long productId) {
        checkMemberValidation(memberId);
        checkProductValidation(productId);
        wishRepository.deleteByMemberIdAndProductId(memberId, productId);
    }

    private void checkProductValidation(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT, productId);
        }
    }

    private void checkMemberValidation(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new CustomException(ErrorCode.INVALID_MEMBER, memberId);
        }
    }

    private Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER, id));
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PRODUCT, id));
    }

    private Wish getWish(Long id) {
        return wishRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_WISH, id));
    }
}
