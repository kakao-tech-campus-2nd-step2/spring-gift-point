package gift.service;

import gift.exception.ErrorCode;
import gift.exception.RepositoryException;
import gift.model.Member;
import gift.model.Product;
import gift.model.WishList;
import gift.model.WishListDTO;
import gift.model.WishListResponse;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishListService {

    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishListService(WishListRepository wishListRepository,
        ProductRepository productRepository, MemberRepository memberRepository) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public WishListResponse createWishList(WishListDTO wishListDTO) {
        Product product = productRepository.findById(wishListDTO.productId())
            .orElseThrow(() -> new RepositoryException(ErrorCode.PRODUCT_NOT_FOUND,
                wishListDTO.productId()));
        Member member = memberRepository.findById(wishListDTO.memberId())
            .orElseThrow(() -> new RepositoryException(ErrorCode.MEMBER_ID_NOT_FOUND,
                wishListDTO.memberId()));
        WishList wishlist = wishListRepository.save(new WishList(member, product));
        return new WishListResponse(wishlist.getId(), wishListDTO.productId());
    }

    @Transactional(readOnly = true)
    public Page<WishListResponse> getWishList(long memberId, Pageable pageable) {
        return wishListRepository.findAllByMemberId(memberId, pageable)
            .map(WishListResponse::convertToDTO);
    }

    @Transactional
    public WishListDTO updateWishListQuantity(WishListDTO wishListDTO) {
        WishList currentWishList = wishListRepository.findByMemberIdAndProductId(
                wishListDTO.memberId(),
                wishListDTO.productId())
            .orElseThrow(() -> new RepositoryException(
                ErrorCode.WISHLIST_NOT_FOUND, wishListDTO.memberId(), wishListDTO.productId()));

        WishList newWishList = new WishList(currentWishList.getMember(),
            currentWishList.getProduct());
        return convertToDTO(wishListRepository.save(newWishList));
    }

    @Transactional
    public void deleteWishList(long memberId, long productId) {
        WishList wishList = wishListRepository.findByMemberIdAndProductId(memberId, productId)
            .orElseThrow(
                () -> new RepositoryException(ErrorCode.WISHLIST_NOT_FOUND, memberId, productId));
        wishListRepository.deleteById(wishList.getId());
    }

    private WishListDTO convertToDTO(WishList wishList) {
        long memberId = wishList.getMember().getId();
        long productId = wishList.getProduct().getId();
        return new WishListDTO(memberId, productId);
    }

}
