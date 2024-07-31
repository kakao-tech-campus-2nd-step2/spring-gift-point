package gift.wishlist.application;

import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.member.dao.MemberRepository;
import gift.member.entity.Member;
import gift.product.dao.OptionRepository;
import gift.product.entity.Option;
import gift.wishlist.dao.WishesRepository;
import gift.wishlist.dto.WishResponse;
import gift.wishlist.entity.Wish;
import gift.wishlist.util.WishMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishesService {

    private final WishesRepository wishesRepository;
    private final MemberRepository memberRepository;
    private final OptionRepository optionRepository;

    public WishesService(WishesRepository wishesRepository,
                         MemberRepository memberRepository,
                         OptionRepository optionRepository) {
        this.wishesRepository = wishesRepository;
        this.memberRepository = memberRepository;
        this.optionRepository = optionRepository;
    }

    public void addProductToWishlist(Long memberId, Long productId) {
        wishesRepository.findByMember_IdAndOption_Id(memberId, productId)
                .ifPresent(wish -> {
                    throw new CustomException(ErrorCode.WISH_ALREADY_EXISTS);
                });

        wishesRepository.save(createWish(memberId, productId));
    }

    public void removeWishIfPresent(Long memberId, Long productId) {
        wishesRepository.findByMember_IdAndOption_Id(memberId, productId)
                .ifPresent(wish -> {
                    wishesRepository.deleteById(wish.getId());
                });
    }

    public void removeWishById(Long id) {
        wishesRepository.deleteById(id);
    }

    public Page<WishResponse> getWishlistOfMember(Long memberId, Pageable pageable) {
        return wishesRepository.findByMember_Id(memberId, pageable)
                .map(WishMapper::toResponseDto);
    }

    private Wish createWish(Long memberId, Long optionId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new CustomException(ErrorCode.OPTION_NOT_FOUND));

        return new Wish(member, option);
    }

}
