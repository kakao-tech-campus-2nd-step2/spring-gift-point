package gift.service;

import gift.dto.Request.WishRequestDto;
import gift.dto.Response.CategoryResponseDto;
import gift.dto.Response.OptionResponseDto;
import gift.dto.Response.ProductResponseDto;
import gift.dto.Response.WishResponseDto;
import gift.model.Option;
import gift.model.Product;
import gift.model.SiteUser;
import gift.model.Wishlist;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    @Autowired
    public WishlistServiceImpl(WishlistRepository wishlistRepository, UserRepository userRepository, ProductRepository productRepository, OptionRepository optionRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
    }

    @Override
    public void addToWish(String username, WishRequestDto wishRequestDto) {
        SiteUser user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Invalid username: " + username));
        Product product = productRepository.findById(wishRequestDto.productId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + wishRequestDto.productId()));

        Wishlist wish = new Wishlist();
        wish.setSiteUser(user);
        wish.setProduct(product);
        wish.setCount(wishRequestDto.count());
        wish.setHidden(false);

        wishlistRepository.save(wish);

        List<Option> options = wishRequestDto.options().stream()
            .map(optionDto -> optionRepository.findById(optionDto.id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID: " + optionDto.id())))
            .collect(Collectors.toList());
        wish.setOptions(options);

        wishlistRepository.save(wish);
    }

    @Override
    public void removeFromWish(Long wishId) {
        wishlistRepository.deleteById(wishId);
    }

    @Override
    public Page<WishResponseDto> getWishesByUser(String username, Pageable pageable) {
        Page<Wishlist> wishEntities = wishlistRepository.findBySiteUserUsernameAndHiddenFalseOrderByCreatedDateDesc(username, pageable);
        return wishEntities.map(this::convertToResponseDto);
    }

    private WishResponseDto convertToResponseDto(Wishlist wish) {
        ProductResponseDto productResponseDto = new ProductResponseDto(
            wish.getProduct().getId(),
            wish.getProduct().getName(),
            wish.getProduct().getPrice(),
            wish.getProduct().getImageUrl(),
            new CategoryResponseDto(
                wish.getProduct().getCategory().getId(),
                wish.getProduct().getCategory().getName(),
                wish.getProduct().getCategory().getColor(),
                wish.getProduct().getCategory().getImageUrl(),
                wish.getProduct().getCategory().getDescription()
            )
        );

        List<OptionResponseDto> options = wish.getOptions().stream()
            .map(option -> new OptionResponseDto(option.getId(), option.getName(), option.getQuantity()))
            .collect(Collectors.toList());

        return new WishResponseDto(
            wish.getId(),
            productResponseDto,
            wish.getCount(),
            options
        );
    }
}
