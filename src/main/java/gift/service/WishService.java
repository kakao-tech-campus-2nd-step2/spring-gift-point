package gift.service;

import gift.common.exception.DuplicateDataException;
import gift.common.exception.EntityNotFoundException;
import gift.controller.dto.request.WishRequest;
import gift.controller.dto.response.PagingResponse;
import gift.controller.dto.response.WishResponse;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository, MemberRepository memberRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void update(WishRequest.UpdateWish request, Long memberId) {
        Wish wish = wishRepository.findByIdFetchJoin(request.id())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 위시리스트입니다."));
        wish.checkWishByMemberId(memberId);
        wish.checkWishByProductId(request.productId());
        if (request.productCount() == 0) {
            deleteById(request.productId(), memberId);
            return;
        }
        wish.updateWish(wish.getMember(), request.productCount(), wish.getProduct());
    }

    @Transactional
    public void save(WishRequest.CreateWish request, int productCount, Long memberId) {
        checkProductByProductId(request.productId());
        checkDuplicateWish(request.productId(), memberId);
        Member member = memberRepository.getReferenceById(memberId);
        Product product = productRepository.getReferenceById(request.productId());
        wishRepository.save(new Wish(member, productCount, product));
    }

    @Transactional(readOnly = true)
    public PagingResponse<WishResponse> findAllWishPagingByMemberId(Long memberId, Pageable pageable) {
        Page<WishResponse> pages = wishRepository.findAllByMemberIdFetchJoin(memberId, pageable)
                .map(WishResponse::from);
        return PagingResponse.from(pages);
    }

    @Transactional
    public void deleteById(Long wishId, Long memberId) {
        Wish wish = wishRepository.findByIdFetchJoin(wishId)
                        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 위시리스트입니다."));
        wish.checkWishByMemberId(memberId);
        wishRepository.deleteById(wishId);
    }

    @Transactional
    public void deleteIfExists(Long productId, Long memberId) {
        if (wishRepository.existsByProductIdAndMemberId(productId, memberId)) {
            wishRepository.deleteByProductIdAndMemberId(productId, memberId);
        }
    }

    private void checkProductByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new EntityNotFoundException("존재하지 않는 상품입니다.");
        }
    }

    private void checkDuplicateWish(Long productId, Long memberId) {
        if (wishRepository.existsByProductIdAndMemberId(productId, memberId)) {
            throw new DuplicateDataException("이미 위시리스트에 추가된 상품입니다.");
        }
    }
}
