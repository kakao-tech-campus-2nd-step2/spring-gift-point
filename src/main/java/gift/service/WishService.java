package gift.service;

import gift.dto.ProductResponseDto;
import gift.dto.WishPageResponseDto;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Product;
import gift.entity.User;
import gift.entity.Wish;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.mapper.ProductMapper;
import gift.mapper.WishMapper;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final ProductService productService;
    private final UserService userService;

    public WishService(WishRepository wishRepository, ProductService productService, UserService userService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
        this.userService = userService;
    }

    @Transactional
    public WishResponseDto addWish(Long userId, WishRequestDto wishRequestDto) {
        User user = userService.getUserEntityById(userId);
        Product product = productService.getProductEntityById(wishRequestDto.getProductId());
        Wish wish = new Wish(user, product);
        Wish createdWish = wishRepository.save(wish);

        return WishMapper.toWishResponseDto(createdWish, ProductMapper.toProductResponseDTO(product));
    }

    @Transactional(readOnly = true)
    public WishPageResponseDto getWishesByUserId(Long userId, Pageable pageable) {
        User user = userService.getUserEntityById(userId);
        Page<Wish> wishes = wishRepository.findByUser(user, pageable);
        List<Long> productIds = wishes.stream()
                .map(wish -> wish.getProduct().getId())
                .collect(Collectors.toList());

        List<ProductResponseDto> products = productService.getProductsByIds(productIds);
        Map<Long, ProductResponseDto> productMap = products.stream()
                .collect(Collectors.toMap(ProductResponseDto::getId, product -> product));

        Page<WishResponseDto> wishResponseDtos = wishes.map(wish -> {
            ProductResponseDto product = productMap.get(wish.getProduct().getId());
            return WishMapper.toWishResponseDto(wish, product);
        });

        return WishPageResponseDto.fromPage(wishResponseDtos);
    }

    @Transactional
    public void deleteWish(Long wishId) {
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "ID: " + wishId));
        wishRepository.delete(wish);
    }
}
