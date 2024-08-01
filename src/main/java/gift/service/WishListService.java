package gift.service;

import gift.Util.JWTUtil;
import gift.dto.wish.WishPageDTO;
import gift.dto.wish.ResponseWishDTO;
import gift.dto.wish.SaveWishlistDTO;
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

import java.util.List;


@Service
@RequiredArgsConstructor
public class WishListService {
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public ResponseWishDTO saveWishList(String token, SaveWishlistDTO saveWishlistDTO) {
        User user = getUserFromToken(token);
        Product product = productRepository.findById(saveWishlistDTO.productId()).orElseThrow(() -> new NotFoundException("해당 물건이없습니다."));
        wishListRepository.findByUserAndProduct(user, product).ifPresent(c -> {
            throw new BadRequestException("이미 추가된 물품입니다.");
        });

        WishList wishList = new WishList(user, product);
        return wishListRepository.save(wishList).toResponseDTO();
    }

    private User getUserFromToken(String token) {
        int tokenUserId = jwtUtil.getUserIdFromToken(token);
        if (!jwtUtil.validateToken(token)) throw new UnAuthException("로그인 만료");

        return userRepository.findById(tokenUserId).orElseThrow(() -> new UnAuthException("인증이 잘못되었습니다"));
    }

    public WishPageDTO getWishList(String token, Pageable pageable) {
        User user = getUserFromToken(token);
        Page<WishList> wishListPage = wishListRepository.findAllByUserId(user.getId(), pageable);

        List<ResponseWishDTO> responseWishDTOs = wishListPage.getContent().stream()
                .map(wishList -> new ResponseWishDTO(
                        wishList.getId(),
                        wishList.getProduct().toResponseDTO()
                ))
                .toList();

        return new WishPageDTO(
                responseWishDTOs,
                wishListPage.getNumber(),
                (int) wishListPage.getTotalElements(),
                wishListPage.getSize(),
                wishListPage.isLast()
        );
    }

    public ResponseWishDTO deleteWishList(String token, int wishId) {
        User user = getUserFromToken(token);
        WishList wishlist = wishListRepository.findById(wishId).orElseThrow(() -> new NotFoundException("존재하지 않는 위시리스트."));
        if (wishlist.getUser().getId() != user.getId()) throw new UnAuthException("해당 위시리스트를 삭제할 권한이 없습니다.");

        user.deleteWishlist(wishlist);
        wishListRepository.deleteById(wishlist.getId());
        return wishlist.toResponseDTO();
    }

}
