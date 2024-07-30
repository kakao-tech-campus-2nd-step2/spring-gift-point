package gift.domain.wish;

import gift.domain.member.JpaMemberRepository;
import gift.domain.member.Member;
import gift.domain.option.JpaOptionRepository;
import gift.domain.option.Option;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import gift.domain.wish.dto.WishPageResponse;
import gift.domain.wish.dto.WishResponse;
import gift.global.exception.product.ProductNotFoundException;
import gift.global.exception.user.MemberNotFoundException;
import gift.global.exception.wish.WishNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {

    private final JpaProductRepository productRepository;
    private final JpaWishRepository wishRepository;
    private final JpaMemberRepository userRepository;
    private final JpaOptionRepository optionRepository;

    public WishService(
        JpaProductRepository jpaProductRepository,
        JpaWishRepository jpaWishRepository,
        JpaMemberRepository jpaMemberRepository,
        JpaOptionRepository jpaOptionRepository
    ) {
        this.userRepository = jpaMemberRepository;
        this.wishRepository = jpaWishRepository;
        this.productRepository = jpaProductRepository;
        this.optionRepository = jpaOptionRepository;
    }

    /**
     * 장바구니에 상품 ID 추가
     */
    @Transactional
    public void addWish(Long memberId, Long productId) {
        Member member = userRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException(memberId));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        // 기존에 존재하면 update
        Optional<Wish> findWish = wishRepository.findByMemberIdAndProductId(memberId,
            productId);
        if (findWish.isPresent()) {
            findWish.get().addOneMore();
            return;
        }
        // 기존에 없었으면 new
        Wish newWish = new Wish(member, product);
        wishRepository.save(newWish);
    }

    /**
     * 장바구니 상품 조회 - 페이징(매개변수별)
     */
    public WishPageResponse getProductsInWish(Long memberId, PageRequest pageRequest) {
        Page<Wish> wishesPage = wishRepository.findAllByMemberId(memberId,
            pageRequest);

        boolean hasNext = wishesPage.hasNext();
        List<WishResponse> wishesResponse = wishesPage.getContent().stream()
            .map(wish -> wish.toWishResponse())
            .toList();

        // 새 Page 객체 생성
        return new WishPageResponse(hasNext, wishesResponse);
    }

    /**
     * 장바구니 상품 삭제`
     */
    public void deleteWish(Long wishId) {
        if (!wishRepository.existsById(wishId)) {
            throw new WishNotFoundException(wishId);
        }
        wishRepository.deleteById(wishId);
    }

    /**
     * 장바구니 상품 수량 수정
     */
    @Transactional
    public void updateWishCount(Long wishId, int count) {
        Wish findWish = wishRepository.findById(wishId)
            .orElseThrow(() -> new WishNotFoundException(wishId));

        findWish.updateCount(count); // 수량 수정
    }

    /**
     * 장바구니에 해당 옵션의 상품이 존재하면 삭제
     */
    public void deleteWishIfExists(Long memberId, Long optionId) {
        Option option = optionRepository.findById(optionId).get();
        wishRepository.findByMemberIdAndProductId(memberId, option.getProduct().getId())
            .ifPresent(wishRepository::delete);
    }
}
