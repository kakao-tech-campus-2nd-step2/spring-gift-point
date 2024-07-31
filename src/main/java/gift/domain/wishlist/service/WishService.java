package gift.domain.wishlist.service;

import gift.domain.member.entity.Member;
import gift.domain.member.exception.MemberNotFoundException;
import gift.domain.member.repository.MemberRepository;
import gift.domain.product.entity.Product;
import gift.domain.product.exception.ProductNotFoundException;
import gift.domain.product.repository.ProductRepository;
import gift.domain.wishlist.dto.WishRequest;
import gift.domain.wishlist.dto.WishResponse;
import gift.domain.wishlist.entity.Wish;
import gift.domain.wishlist.exception.WishDuplicateException;
import gift.domain.wishlist.exception.WishNotFoundException;
import gift.domain.wishlist.repository.WishRepository;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, MemberRepository memberRepository,
        ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;

    }

    public Page<WishResponse> getWishesByMember(Member member, Pageable pageable) {
        return wishRepository
            .findAllByMember(member, pageable)
            .map(this::entityToDto);
    }

    @Transactional
    public void createWish(WishRequest wishRequest) {
        Member member = memberRepository.findById(wishRequest.getMemberId())
            .orElseThrow(() -> new MemberNotFoundException("해당 유저가 존재하지 않습니다."));
        Product product = productRepository.findById(wishRequest.getProductId())
            .orElseThrow(() -> new ProductNotFoundException("해당 상품이 존재하지 않습니다."));

        if (wishRepository.existsByProductAndMember(product, member)) {
            throw new WishDuplicateException("중복된 위시리스트 입니다.");
        }

        Wish wish = new Wish(member, product, LocalDateTime.now());
        wish.getMember().addWish(wish);
    }

    @Transactional
    public void deleteWish(Long id, Member member) {
        Product savedProduct = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("해당 상품이 존재하지 않습니다."));
        Wish wish = wishRepository
            .findByProductAndMember(savedProduct, member)
            .orElseThrow(() -> new WishNotFoundException("해당 위시리스트가 존재하지 않습니다."));

        if (wish.getMember().getId().equals(member.getId())) {
            wish.getMember().removeWish(wish);
            wishRepository.delete(wish);
        }
    }

    private WishResponse entityToDto(Wish wish) {
        Product productInWish = productRepository.findById(wish.getId()).orElseThrow(()-> new ProductNotFoundException("상품이 존재하지 않습니다."));
        return new WishResponse(productInWish.getId(), productInWish.getName(),
            productInWish.getPrice(), productInWish.getImageUrl());
    }
}
