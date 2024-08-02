package gift.product.service;

import gift.product.dto.auth.LoginMemberIdDto;
import gift.product.dto.wish.WishDto;
import gift.product.dto.wish.WishResponse;
import gift.product.model.Member;
import gift.product.model.Product;
import gift.product.model.Wish;
import gift.product.repository.AuthRepository;
import gift.product.repository.ProductRepository;
import gift.product.repository.WishRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final AuthRepository authRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository,
        AuthRepository authRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.authRepository = authRepository;
    }

    private static WishResponse getWishResponse(Wish wish) {
        return new WishResponse(wish.getProduct().getId(),
            wish.getProduct().getName(),
            wish.getProduct().getPrice(),
            wish.getProduct().getImageUrl());
    }

    public List<Wish> getWishAll(LoginMemberIdDto loginMemberIdDto) {
        return wishRepository.findAllByMemberId(loginMemberIdDto.id());
    }

    public Page<WishResponse> getWishAll(Pageable pageable, LoginMemberIdDto loginMemberIdDto) {
        return wishRepository.findAllByMemberId(pageable, loginMemberIdDto.id())
            .map(WishService::getWishResponse);
    }

    public Wish getWish(Long id, LoginMemberIdDto loginMemberIdDto) {
        return getValidatedWish(id, loginMemberIdDto);
    }

    @Transactional
    public Wish insertWish(WishDto wishDto, LoginMemberIdDto loginMemberIdDto) {
        Product product = getValidatedProduct(wishDto.productId());
        validateRedundancyWishByProductIdAndMemberId(wishDto.productId(), loginMemberIdDto.id());

        Member member = getMember(loginMemberIdDto);
        Wish wish = new Wish(member, product);
        return wishRepository.save(wish);
    }

    @Transactional
    public void deleteWish(Long id, LoginMemberIdDto loginMemberIdDto) {
        getValidatedWish(id, loginMemberIdDto);
        wishRepository.deleteByIdAndMemberId(id, loginMemberIdDto.id());
    }

    private Member getMember(LoginMemberIdDto loginMemberIdDto) {
        return authRepository.findById(loginMemberIdDto.id())
            .orElseThrow(() -> new NoSuchElementException("회원 정보가 존재하지 않습니다."));
    }

    private Wish getValidatedWish(Long id, LoginMemberIdDto loginMemberIdDto) {
        return wishRepository.findByIdAndMemberId(id, loginMemberIdDto.id())
            .orElseThrow(() -> new NoSuchElementException("해당 ID의 위시 항목이 위시리스트에 존재하지 않습니다."));
    }

    private Product getValidatedProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("해당 ID의 상품이 존재하지 않습니다."));
    }

    private void validateRedundancyWishByProductIdAndMemberId(Long productId, Long memberId) {
        if (wishRepository.existsByProductIdAndMemberId(productId, memberId)) {
            throw new IllegalArgumentException("해당 상품이 이미 위시 리스트에 존재합니다.");
        }
    }
}
