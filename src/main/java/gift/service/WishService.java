package gift.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.request.MemberRequest;
import gift.dto.request.WishRequest;
import gift.dto.response.ProductResponse;
import gift.dto.response.WishAddResponse;
import gift.dto.response.WishResponse;
import gift.exception.customException.ProductAlreadyInWishlistException;
import gift.exception.customException.WishNotFoundException;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static gift.exception.errorMessage.Messages.*;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final ProductService productService;

    public WishService(WishRepository wishRepository, ProductService productService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
    }

    @Transactional
    public WishAddResponse save(MemberRequest memberRequest, WishRequest wishRequest) {
        if (wishRepository.existsByProductId(wishRequest.productId())) {
            throw new ProductAlreadyInWishlistException(PRODUCT_ALREADY_IN_WISHLIST);
        }

        ProductResponse productResponse = productService.findById(wishRequest.productId());
        Product product = productService.toEntity(productResponse);
        Member member = memberRequest.toEntity();

        LocalDateTime createdDate = LocalDateTime.now();
        Wish newWish = wishRepository.save(new Wish(member, product, createdDate));
        return new WishAddResponse(newWish.getId(), newWish.getProduct().getId());
    }

    @Transactional(readOnly = true)
    public Page<WishResponse> getPagedMemberWishesByMemberId(Long memberId, Pageable pageable) {
        Page<Wish> wishPage = wishRepository.findByMemberId(memberId, pageable);
        return wishPage.map(WishResponse::from);
    }

    @Transactional
    public void deleteById(Long id) {
        Wish foundWish = wishRepository.findById(id)
                .orElseThrow(() -> new WishNotFoundException(NOT_FOUND_WISH));

        foundWish.remove();
        wishRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByProductId(Long productId) {
        return wishRepository.existsByProductId(productId);
    }

    @Transactional
    public void deleteByProductId(Long optionId) {
        wishRepository.deleteByProductId(optionId);
    }
}
