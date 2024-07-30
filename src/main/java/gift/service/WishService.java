package gift.service;

import gift.exception.product.NotFoundProductException;
import gift.exception.wishlist.DuplicateWishException;
import gift.exception.member.NotFoundMemberException;
import gift.exception.wishlist.NotFoundWishException;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import gift.response.ProductResponse;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishService(WishRepository wishRepository,
        ProductRepository productRepository, MemberRepository memberRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public List<ProductResponse> getMyWishList(Long memberId) {
        return wishRepository.findAllByMemberId(memberId)
            .stream()
            .map(ProductResponse::createProductResponse)
            .toList();
    }

    public Page<Product> getPagedWishList(Long memberId, Pageable pageable) {
        return wishRepository.findPageBy(memberId, pageable);
    }

    @Transactional
    public void addMyWish(Long memberId, Long productId) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(NotFoundMemberException::new);
        Product product = productRepository.findById(productId)
            .orElseThrow(NotFoundProductException::new);

        try {
            Wish wish = new Wish(member, product);
            wishRepository.save(wish);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateWishException();
        }
    }

    @Transactional
    public void deleteMyWish(Long wishId) {
        wishRepository.findById(wishId)
            .ifPresentOrElse(
                wishRepository::delete,
                () -> {
                    throw new NotFoundWishException();
                }
            );
    }


}
