package gift.product.service;

import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;
import static gift.product.exception.GlobalExceptionHandler.NO_PERMISSION;

import gift.product.dto.WishRequestDTO;
import gift.product.dto.WishResponseDTO;
import gift.product.exception.InvalidIdException;
import gift.product.exception.UnauthorizedException;
import gift.product.model.Member;
import gift.product.model.Product;
import gift.product.model.Wish;
import gift.product.repository.ProductRepository;
import gift.product.repository.WishListRepository;
import gift.product.util.JwtUtil;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;
    private final JwtUtil jwtUtil;
    private final ProductRepository productRepository;

    @Autowired
    public WishListService(
        WishListRepository wishListRepository,
        JwtUtil jwtUtil,
        ProductRepository productRepository
    ) {
        this.wishListRepository = wishListRepository;
        this.jwtUtil = jwtUtil;
        this.productRepository = productRepository;
    }

    public Page<WishResponseDTO> getAllWishes(String authorization, Pageable pageable) {
        System.out.println("[WishListService] getAllProducts()");
        Long memberId = jwtUtil.parsingToken(authorization).getId();
        return wishListRepository.findAllByMemberId(memberId, pageable);
    }

    public WishResponseDTO registerWishProduct(String authorization, WishRequestDTO wishRequestDTO) {
        System.out.println("[WishListService] registerWishProduct()");
        Member member = jwtUtil.parsingToken(authorization);
        Product product = productRepository.findById(wishRequestDTO.getProductId())
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
        Wish wish = wishListRepository.save(wishRequestDTO.convertToDomain(member, product));
        return new WishResponseDTO(wish.getId(), wish.getProduct());
    }

    public void deleteWishProduct(String authorization, Long id) {
        System.out.println("[WishListService] deleteWishProduct()");
        Member member = jwtUtil.parsingToken(authorization);
        Wish wish = wishListRepository.findById(id)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
        if(!Objects.equals(wish.getMember(), member))
            throw new UnauthorizedException(NO_PERMISSION);
        wishListRepository.deleteById(id);
    }
}
