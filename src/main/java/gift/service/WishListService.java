package gift.service;

import gift.exception.ErrorCode;
import gift.exception.RepositoryException;
import gift.model.Member;
import gift.model.Product;
import gift.model.WishList;
import gift.model.WishListDTO;
import gift.model.WishListPageDTO;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public WishListDTO createWishList(WishListDTO wishListDTO) {
        Product product = productRepository.findById(wishListDTO.productId())
            .orElseThrow(() -> new RepositoryException(ErrorCode.PRODUCT_NOT_FOUND,
                wishListDTO.productId()));
        Member member = memberRepository.findById(wishListDTO.memberId())
            .orElseThrow(() -> new RepositoryException(ErrorCode.MEMBER_ID_NOT_FOUND,
                wishListDTO.memberId()));
        WishList wishList = new WishList(member, product, wishListDTO.quantity());
        return convertToDTO(wishListRepository.save(wishList));
    }

    public WishListPageDTO getWishListByMemberId(long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<WishListDTO> wishListPage = wishListRepository.findWishListByMemberId(memberId,
                pageable)
            .map(this::convertToDTO)
            .stream()
            .toList();

        return new WishListPageDTO(page, size, wishListPage.size(), wishListPage);
    }

    public WishListDTO updateWishListQuantity(WishListDTO wishListDTO) {
        WishList currentWishList = wishListRepository.findByMemberIdAndProductId(
                wishListDTO.memberId(),
                wishListDTO.productId())
            .orElseThrow(() -> new RepositoryException(
                ErrorCode.WISHLIST_NOT_FOUND, wishListDTO.memberId(), wishListDTO.productId()));

        WishList newWishList = new WishList(currentWishList.getId(), currentWishList.getMember(),
            currentWishList.getProduct(), wishListDTO.quantity());
        return convertToDTO(wishListRepository.save(newWishList));
    }

    public void deleteWishList(long memberId, long productId) {
        WishList wishList = wishListRepository.findByMemberIdAndProductId(memberId, productId)
            .orElseThrow(
                () -> new RepositoryException(ErrorCode.WISHLIST_NOT_FOUND, memberId, productId));
        wishListRepository.deleteById(wishList.getId());
    }

    private WishListDTO convertToDTO(WishList wishList) {
        long memberId = wishList.getMember().getId();
        long productId = wishList.getProduct().getId();
        return new WishListDTO(memberId, productId, wishList.getQuantity());
    }

}
