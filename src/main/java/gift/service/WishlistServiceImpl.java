package gift.service;

import gift.dto.Request.OptionRequest;
import gift.dto.Response.WishlistResponse;
import gift.dto.WishlistDTO;
import gift.model.Option;
import gift.model.Product;
import gift.model.SiteUser;
import gift.model.Wishlist;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public List<WishlistDTO> getWishlistByUser(String username) {
        List<Wishlist> wishlistEntities = wishlistRepository.findByUserUsernameAndHiddenFalse(username);
        return wishlistEntities.stream()
            .map(WishlistDTO::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public WishlistResponse addToWishlist(String username, Long productId, int quantity, List<OptionRequest> options) {
        SiteUser user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Invalid username: " + username));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + productId));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlist.setQuantity(quantity);
        wishlist.setPrice(product.getPrice());

        List<Option> optionEntities = getOptionEntities(options);
        wishlist.setOptions(optionEntities);

        wishlistRepository.save(wishlist);
        return new WishlistResponse(true);
    }

    private List<Option> getOptionEntities(List<OptionRequest> options) {
        return options.stream()
            .map(optionRequest -> {
                Option optionEntity = optionRepository.findById(optionRequest.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid option ID: " + optionRequest.getId()));
                optionEntity.setQuantity(optionRequest.getQuantity());
                return optionEntity;
            }).collect(Collectors.toList());
    }

    @Override
    public WishlistResponse removeFromWishlist(Long id) {
        wishlistRepository.deleteById(id);
        return new WishlistResponse(true);
    }

    @Override
    public WishlistResponse updateQuantity(Long id, int quantity, Long optionId) {
        Wishlist wishlist = wishlistRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID: " + id));

        if (optionId != null) {
            List<Option> options = wishlist.getOptions();
            options.stream()
                .filter(option -> option.getId().equals(optionId))
                .forEach(option -> option.setQuantity(quantity));
        } else {
            wishlist.setQuantity(quantity);
        }

        wishlistRepository.save(wishlist);
        return new WishlistResponse(true);
    }

    @Override
    public Page<WishlistDTO> getWishlistByUser1(String username, Pageable pageable) {
        Page<Wishlist> wishlistEntities = wishlistRepository.findByUserUsernameAndHiddenFalse(username, pageable);
        return wishlistEntities.map(WishlistDTO::convertToDTO);
    }

    @Override
    public WishlistDTO getWishlistById(Long id) {
        Wishlist wishlist = wishlistRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID: " + id));
        return WishlistDTO.convertToDTO(wishlist);
    }

    @Override
    public void orderWishlist(Long id) {
        Wishlist wishlist = wishlistRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID: " + id));

        for (Option option : wishlist.getOptions()) {
            option.setMaxQuantity(option.getMaxQuantity() - option.getQuantity());
            optionRepository.save(option);
        }

        wishlist.setHidden(true);
        wishlistRepository.save(wishlist);
    }
}
