package gift.wish.service;

import gift.member.domain.Member;
import gift.member.service.MemberService;
import gift.product.domain.Product;
import gift.product.service.ProductService;
import gift.wish.domain.Wish;
import gift.wish.dto.WishResponseDto;
import gift.wish.dto.WishResponseListDto;
import gift.wish.dto.WishServiceDto;
import gift.wish.exception.WishNotFoundException;
import gift.wish.repository.WishRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final MemberService memberService;
    private final ProductService productService;
    private static final int WISH_SIZE = 10;

    public WishService(WishRepository wishRepository, MemberService memberService, ProductService productService) {
        this.wishRepository = wishRepository;
        this.memberService = memberService;
        this.productService = productService;
    }

    public List<WishResponseDto> getAllWishesByMember(Member member) {
        return WishResponseDto.wishListToWishResponseList(
                wishRepository.findAllByMemberId(member.getId()));
    }

    public WishResponseListDto getWishesByMemberAndPage(Member member, int page) {
        return WishResponseListDto.wishPageToWishResponseListDto(
                wishRepository.findAllByMemberId(member.getId(), PageRequest.of(page, WISH_SIZE)));
    }

    public WishResponseDto getWishById(Long id) {
        return new WishResponseDto(wishRepository.findById(id)
                .orElseThrow(WishNotFoundException::new));
    }

    public Wish createWish(WishServiceDto wishServiceDto) {
        return wishRepository.save(findOrCreateWish(wishServiceDto));
    }

    private Wish findOrCreateWish(WishServiceDto wishServiceDto) {
        // Wish가 존재하지 않으면 새로운 Wish 생성
        Wish wish = wishRepository.findByMemberIdAndProductId(wishServiceDto.memberId(), wishServiceDto.productId())
                .orElseGet(() -> getWishByWishServiceDto(wishServiceDto));

        if (!wish.checkNew()) {
            // Wish가 이미 존재하면 productCount 증가
            wish.increaseProductCount(wishServiceDto.productCount());
        }

        return wish;
    }

    public Wish updateWish(WishServiceDto wishServiceDto) {
        validateWishExists(wishServiceDto.id());
        return wishRepository.save(getWishByWishServiceDto(wishServiceDto));
    }

    public void deleteWish(Long id) {
        validateWishExists(id);
        wishRepository.deleteById(id);
    }

    private Wish getWishByWishServiceDto(WishServiceDto wishServiceDto) {
        Member member = memberService.getMemberById(wishServiceDto.memberId());
        Product product = productService.getProductById(wishServiceDto.productId());
        return wishServiceDto.toWish(member, product);
    }

    private void validateWishExists(Long id) {
        wishRepository.findById(id)
                .orElseThrow(WishNotFoundException::new);
    }
}
