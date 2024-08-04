package gift.application.wish;

import gift.model.member.Member;
import gift.model.product.Option;
import gift.model.product.Product;
import gift.model.wish.Wish;
import gift.repository.member.MemberRepository;
import gift.repository.product.OptionRepository;
import gift.repository.product.ProductRepository;
import gift.repository.wish.WishJpaRepository;
import gift.global.validate.NotFoundException;
import gift.repository.wish.WishRepository;
import gift.application.wish.dto.WishCommand;
import gift.application.wish.dto.WishModel;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    public WishService(WishJpaRepository wishRepository, MemberRepository memberRepository,
        ProductRepository productRepository,
        OptionRepository optionRepository) {
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional
    public WishModel.Info addWish(Long userId, WishCommand.Register command) {
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("Member not found"));

        Product product = productRepository.findById(command.productId())
            .orElseThrow(() -> new NotFoundException("Product not found"));

        wishRepository.findByMemberAndProduct(member, product)
            .ifPresent(wish -> {
                throw new IllegalArgumentException("Wish already exists");
            });

        var wish = wishRepository.save(command.toEntity(member, product));
        return WishModel.Info.from(wish);
    }

    @Transactional
    public WishModel.Info updateWish(Long memberId, WishCommand.Update command) {
        Wish wish = wishRepository.findByMemberIdAndProductId(memberId, command.productId())
            .orElseThrow(() -> new NotFoundException("Wish not found"));

        wish.updateCount(command.count());

        return WishModel.Info.from(wish);
    }

    @Transactional
    public void deleteWish(Long memberId, Long wishId) {
        Wish wish = wishRepository.findById(wishId)
            .orElseThrow(() -> new NotFoundException("Wish not found"));

        if (wish.isOwner(memberId)) {
            wishRepository.deleteById(wishId);
            return;
        }

        throw new IllegalArgumentException("본인의 위시리스트만 삭제 가능합니다.");
    }

    @Transactional(readOnly = true)
    public Page<WishModel.Info> getWishesPaging(Long memberId, Pageable pageable) {
        Page<Wish> wishPage = wishRepository.findAllByMemberByIdDesc(memberId, pageable);

        return wishPage.map(WishModel.Info::from);
    }

    @Transactional
    public void deleteWishByOption(Long memberId, Long optionId) {
        Option option = optionRepository.findById(optionId).orElseThrow(
            () -> new NotFoundException("Option not found")
        );
        Long productId = option.getProduct().getId();
        wishRepository.findByMemberIdAndProductId(memberId, productId)
            .ifPresent(w -> wishRepository.deleteById(w.getId()));
    }
}
