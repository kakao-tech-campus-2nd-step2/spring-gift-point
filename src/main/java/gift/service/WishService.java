package gift.service;

import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.ServiceException;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
        Member member = memberService.getMemberById(memberId);
        Page<Wish> wishes = wishRepository.findByMemberId(memberId, pageable);
        return wishes.map(WishResponseDto::new);
    }

    public void addWishlist(Long memberId, Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ServiceException("존재하지 않는 상품입니다.", HttpStatus.NOT_FOUND));

        if (wishRepository.existsByMemberIdAndProductId(memberId, productId)) {
            throw new ServiceException("이미 위시리스트에 존재하는 상품입니다.", HttpStatus.CONFLICT);
        }

        Member member = memberService.getMemberById(memberId);
        Wish wish = new Wish(member, product);

        wishRepository.save(wish);
    }

    public void deleteById(Long memberId, Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ServiceException("존재하지 않는 상품입니다.", HttpStatus.NOT_FOUND));

        Wish wish = wishRepository.findByMemberIdAndProductId(memberId, productId)
            .orElseThrow(() -> new ServiceException("위시리스트에 존재하지 않는 상품입니다.", HttpStatus.NOT_FOUND));

        wishRepository.delete(wish);
    }
}