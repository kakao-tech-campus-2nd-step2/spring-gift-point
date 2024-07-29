package gift.wishlist.service;

import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.wishlist.dto.WishListReqDto;
import gift.wishlist.dto.WishListResDto;
import gift.wishlist.entity.WishList;
import gift.wishlist.exception.WishListCreateException;
import gift.wishlist.exception.WishListDeleteException;
import gift.wishlist.exception.WishListNotFoundException;
import gift.wishlist.exception.WishListUpdateException;
import gift.wishlist.repository.WishListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishListService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final WishListRepository wishListRepository;

    public WishListService(MemberRepository memberRepository, ProductRepository productRepository, WishListRepository wishListRepository) {
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.wishListRepository = wishListRepository;
    }

    @Transactional(readOnly = true)
    public Page<WishListResDto> getWishListsByMemberId(Long id, Pageable pageable) {
        Page<WishList> wishLists = wishListRepository.findAllByMemberId(id, pageable);
        return wishLists.map(WishListResDto::new);
    }

    @Transactional
    public void addWishList(Long memberId, WishListReqDto wishListReqDto) {
        // 이미 위시 리스트에 있는 상품이면 수량을 더한다.
        if (wishListRepository.existsByMemberIdAndProductId(memberId, wishListReqDto.productId())) {
            addQuantity(memberId, wishListReqDto.productId(), wishListReqDto.quantity());
            return;
        }

        Product product = productRepository.findById(wishListReqDto.productId()).orElseThrow(
                () -> WishListNotFoundException.EXCEPTION
        );

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> WishListNotFoundException.EXCEPTION
        );

        try {
            WishList wishList = new WishList(member, product, wishListReqDto.quantity());
            wishListRepository.save(wishList);
        } catch (Exception e) {
            throw WishListCreateException.EXCEPTION;
        }
    }

    private void addQuantity(Long memberId, Long productId, Integer quantity) {
        try {
            WishList findWishList = wishListRepository.findByMemberIdAndProductId(memberId, productId).orElseThrow(
                    () -> WishListNotFoundException.EXCEPTION
            );
            findWishList.addQuantity(quantity);
        } catch (Exception e) {
            throw WishListUpdateException.EXCEPTION;
        }
    }

    @Transactional
    public void updateWishListById(Long memberId, Long wishListId, WishListReqDto wishListReqDto) {
        // 수량이 0이면 삭제
        Integer quantity = wishListReqDto.quantity();
        if (quantity == 0) {
            deleteWishListById(memberId, wishListId);
            return;
        }

        WishList findWishList = findByIdAndMemberIdOrThrow(memberId, wishListId);
        try {
            findWishList.changeQuantity(quantity);
        } catch (Exception e) {
            throw WishListUpdateException.EXCEPTION;
        }
    }

    @Transactional
    public void deleteWishListById(Long memberId, Long wishListId) {
        WishList findWishList = findByIdAndMemberIdOrThrow(memberId, wishListId);
        try {
            Member member = memberRepository.findById(memberId).get();  // 회원의 존재 여부는 요청 검증 과정을 거치므로 신뢰할 수 있음
            member.deleteWishList(findWishList);
        } catch (Exception e) {
            throw WishListDeleteException.EXCEPTION;
        }
    }

    private WishList findByIdAndMemberIdOrThrow(Long memberId, Long wishListId) {
        return wishListRepository.findByIdAndMemberId(wishListId, memberId)
                .orElseThrow(() -> WishListNotFoundException.EXCEPTION);
    }
}
