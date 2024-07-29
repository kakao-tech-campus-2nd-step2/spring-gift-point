package gift.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.response.WishResponseDto;
import gift.exception.customException.EntityNotFoundException;
import gift.exception.customException.ForbiddenException;
import gift.repository.member.MemberRepository;
import gift.repository.product.ProductRepository;
import gift.repository.wish.WishRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static gift.exception.exceptionMessage.ExceptionMessage.*;

@Service
@Transactional(readOnly = true)
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    private final MemberRepository memberRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository, MemberRepository memberRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }


    public List<WishResponseDto> findAllWish(String email) {
        return wishRepository.findWishByByMemberEmail(email).stream()
                .map(WishResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<WishResponseDto> findWishesPaging(String email, Pageable pageable) {
        return wishRepository.findWishesByMemberEmail(email, pageable).stream()
                .map(WishResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public WishResponseDto addWish(Long productId, String email, int count) {
        Product findProduct = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        Member findMember = memberRepository.findMemberByEmail(email).orElseThrow(() -> new EntityNotFoundException(NOT_EXISTS_MEMBER));

        Wish wish = new Wish.Builder()
                .product(findProduct)
                .member(findMember)
                .count(count)
                .build();

        Wish savedWish = wishRepository.save(wish);

        return WishResponseDto.from(savedWish);
    }

    @Transactional
    public WishResponseDto editWish(Long wishId, String email, int count) {
        wishRepository.findById(wishId).orElseThrow(() -> new EntityNotFoundException(WISH_NOT_FOUND));

        Wish findWish = wishRepository.findWishByIdAndMemberEmail(wishId, email).orElseThrow(ForbiddenException::new);

        findWish.update(count);

        return WishResponseDto.from(findWish);
    }

    @Transactional
    public WishResponseDto deleteWish(Long wishId, String email) {
        wishRepository.findById(wishId).orElseThrow(() -> new EntityNotFoundException(WISH_NOT_FOUND));

        Wish findWish = wishRepository.findWishByIdAndMemberEmail(wishId, email).orElseThrow(ForbiddenException::new);

        wishRepository.delete(findWish);

        return WishResponseDto.from(findWish);
    }
}
