package gift.services;


import gift.classes.Exceptions.ProductException;
import gift.classes.Exceptions.WishException;
import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.MemberDto;
import gift.dto.PageInfoDto;
import gift.dto.WishDto;
import gift.dto.WishPageDto;
import gift.repositories.MemberRepository;
import gift.repositories.ProductRepository;
import gift.repositories.WishRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public WishService(WishRepository wishRepository, ProductRepository productRepository,
        MemberRepository memberRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    //    Wishlist 조회
    @Transactional
    public WishPageDto getWishListById(Long memberId, int page, int size) {
        if (page < 0 || size <= 0) {
            throw new WishException(
                "Page must be non-negative and size must be greater than zero. ");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Wish> wishList = wishRepository.findAllByMemberId(memberId, pageable);
        List<WishDto> wishDtoList = wishList.stream()
            .map(Wish::toWishDto)
            .toList();

        PageInfoDto pageInfo = new PageInfoDto(page, wishList.getTotalElements(),
            wishList.getTotalPages());

        return new WishPageDto(pageInfo, wishDtoList);
    }

    //    Wish 추가
    @Transactional
    public WishDto addWish(MemberDto memberDto, Long productId) {
        Member member = memberRepository.findByEmail(memberDto.getEmail());
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductException("Product with ID " + productId + " not found"));

        Wish wish = new Wish(member, product);
        Wish addedWish = wishRepository.save(wish);
        return addedWish.toWishDto();
    }

    //    Wish 삭제
    @Transactional
    public void deleteWish(Long wishId) {
        wishRepository.deleteById(wishId);
    }

}
