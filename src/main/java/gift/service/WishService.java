package gift.service;

import static gift.controller.wish.WishMapper.toWishResponse;

import gift.controller.wish.WishCreateRequest;
import gift.controller.wish.WishMapper;
import gift.controller.wish.WishResponse;
import gift.controller.wish.WishUpdateRequest;
import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.exception.MemberAlreadyExistsException;
import gift.exception.MemberNotExistsException;
import gift.exception.ProductNotExistsException;
import gift.exception.WishNotExistsException;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final WishRepository wishRepository;

    public WishService(ProductRepository productRepository, MemberRepository memberRepository,
        WishRepository wishRepository) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.wishRepository = wishRepository;
    }

    @Transactional(readOnly = true)
    public Page<WishResponse> findAll(Pageable pageable) {
        Page<Wish> wishPage = wishRepository.findAll(pageable);
        List<WishResponse> wishResponses = wishPage.stream().map(WishMapper::toWishResponse)
            .toList();
        return new PageImpl<>(wishResponses, pageable, wishPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<WishResponse> findAllByMemberId(UUID memberId, Pageable pageable) {
        Page<Wish> wishPage = wishRepository.findAllByMemberId(memberId, pageable);
        List<WishResponse> wishResponses = wishPage.stream().map(WishMapper::toWishResponse)
            .toList();
        return new PageImpl<>(wishResponses, pageable, wishPage.getTotalElements());
    }

    @Transactional
    public WishResponse save(UUID memberId, WishCreateRequest wish) {
        wishRepository.findByMemberIdAndProductId(memberId, wish.productId()).ifPresent(p -> {
            throw new MemberAlreadyExistsException();
        });
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotExistsException::new);
        Product product = productRepository.findById(wish.productId())
            .orElseThrow(ProductNotExistsException::new);

        return toWishResponse(wishRepository.save(new Wish(member, product, wish.count())));
    }

    @Transactional
    public WishResponse update(UUID memberId, UUID productId, WishUpdateRequest wish) {
        Wish target = wishRepository.findByMemberIdAndProductId(memberId, productId)
            .orElseThrow(WishNotExistsException::new);
        target.setCount(wish.count());
        return toWishResponse(wishRepository.save(target));
    }

    @Transactional
    public void delete(UUID memberId, UUID productId) {
        wishRepository.findByMemberIdAndProductId(memberId, productId)
            .orElseThrow(WishNotExistsException::new);
        wishRepository.deleteByMemberIdAndProductId(memberId, productId);
    }
}