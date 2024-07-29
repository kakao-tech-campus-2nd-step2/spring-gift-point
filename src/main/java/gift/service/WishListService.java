package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import gift.Util.JWTUtil;
import gift.dto.product.ShowProductDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class WishListService {
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public void saveWishList(String token, int productId) {
        User user = getUserFromToken(token);
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("해당 물건이없습니다."));
        wishListRepository.findByUserAndProduct(user, product).ifPresent(c -> {
            throw new BadRequestException("이미 추가된 물품입니다.");
        });

        WishList wishList = new WishList(user, product);
        wishListRepository.save(wishList);
    }

    private User getUserFromToken(String token) {
        int tokenUserId = jwtUtil.getUserIdFromToken(token);
        if (!jwtUtil.validateToken(token)) throw new UnAuthException("로그인 만료");

        return userRepository.findById(tokenUserId).orElseThrow(() -> new UnAuthException("인증이 잘못되었습니다"));
    }

    public Page<ShowProductDTO> getWishList(String token, Pageable pageable) {
        User user = getUserFromToken(token);
        return wishListRepository.findByUserId(user.getId(), pageable);
    }

    public void deleteWishList(String token, int productId) {
        User user = getUserFromToken(token);
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("해당 물건이없습니다."));
        WishList wishlist = wishListRepository.findByUserAndProduct(user, product).orElseThrow(() -> new BadRequestException("이미 추가된 물품입니다."));

        product.deleteWishlist(wishlist);
        user.deleteWishlist(wishlist);
        wishListRepository.deleteById(wishlist.getId());
    }

}
