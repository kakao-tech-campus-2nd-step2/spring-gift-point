package gift.service;

import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.BusinessException;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final MemberService memberService;
    private final ProductRepository productRepository;

    @Autowired
    public WishService(WishRepository wishRepository, MemberService memberService,
        ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.memberService = memberService;
        this.productRepository = productRepository;
    }

    public Page<WishResponseDto> getWishlist(Long memberId, Pageable pageable) {
        Page<Wish> wishes = wishRepository.findByMemberId(memberId, pageable);
        return wishes.map(wish -> {
            WishResponseDto response = new WishResponseDto();
            response.setId(wish.getId());
            response.setProductId(wish.getProduct().getId());
            response.setProductName(wish.getProduct().getName());
            response.setProductImageUrl(wish.getProduct().getImageUrl());
            response.setProductQuantity(wish.getQuantity());
            return response;
        });
    }

    public void addWishlist(Long memberId, Long productId, int quantity) {
        Optional<Wish> wishlists = wishRepository.findByMemberIdAndProductId(memberId,
            productId);
        Member member = memberService.getMemberById(memberId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new BusinessException("해당 id에 대한 상품 정보가 없습니다."));
        Wish wish = new Wish(member, product, quantity);

        if (wishlists.isPresent()) {
            Wish existingWish = wishlists.get();
            existingWish.setQuantity(quantity);
            wishRepository.save(existingWish);
            return;
        }

        wishRepository.save(wish);
    }

    public void deleteById(Long memberId, Long productId) {
        Wish wish = wishRepository.findByMemberIdAndProductId(memberId, productId)
            .orElseThrow(() -> new BusinessException("삭제할 아이템을 찾을 수 없습니다."));
        wishRepository.delete(wish);
    }
}