package gift.service;

import gift.Util.JWTUtil;
import gift.dto.wish.ResponseWishDTO;
import gift.dto.wish.SaveWishlistDTO;
import gift.dto.wish.WishPageDTO;
import gift.entity.Product;
import gift.entity.User;
import gift.entity.WishList;
import gift.exception.exception.BadRequestException;
import gift.exception.exception.NotFoundException;
import gift.exception.exception.UnAuthException;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WishListService {
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ResponseWishDTO saveWishList(String token, SaveWishlistDTO saveWishlistDTO) {
        User user = getUserFromToken(token);
        Product product = findProductById(saveWishlistDTO);
        wishListRepository.findByUserAndProduct(user, product).ifPresent(c -> {
            throw new BadRequestException("이미 추가된 물품입니다.");
        });

        WishList wishList = new WishList(user, product);
        return wishListRepository.save(wishList).toResponseDTO();
    }

    private Product findProductById(SaveWishlistDTO saveWishlistDTO) {
        return productRepository.findById(saveWishlistDTO.productId()).orElseThrow(() -> new NotFoundException("해당 물건이없습니다."));
    }

    private User getUserFromToken(String token) {
        int tokenUserId = JWTUtil.getUserIdFromToken(token);
        if (!JWTUtil.validateToken(token)) throw new UnAuthException("로그인 만료");

        return userRepository.findById(tokenUserId).orElseThrow(() -> new UnAuthException("인증이 잘못되었습니다"));
    }

    public WishPageDTO getWishList(String token, Pageable pageable) {
        User user = getUserFromToken(token);
        Page<WishList> wishListPage = wishListRepository.findAllByUserId(user.getId(), pageable);

        return new WishPageDTO(wishListPage);
    }

    public ResponseWishDTO deleteWishList(String token, int wishId) {
        User user = getUserFromToken(token);
        WishList wishlist = findWishlistById(wishId);
        if (wishlist.getUser().getId() != user.getId()) throw new UnAuthException("해당 위시리스트를 삭제할 권한이 없습니다.");

        user.deleteWishlist(wishlist);
        wishListRepository.deleteById(wishlist.getId());
        return wishlist.toResponseDTO();
    }

    private WishList findWishlistById(int wishId) {
        return wishListRepository.findById(wishId).orElseThrow(() -> new NotFoundException("존재하지 않는 위시리스트."));
    }

}
