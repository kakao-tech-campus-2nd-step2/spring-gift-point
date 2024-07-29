package gift.service;

import gift.converter.WishListConverter;
import gift.dto.PageRequestDTO;
import gift.dto.WishListDTO;
import gift.model.Product;
import gift.model.User;
import gift.model.WishList;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishListService {

    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishListService(WishListRepository wishListRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Page<WishListDTO> getWishListByUser(String email, PageRequestDTO pageRequestDTO) {
        User user = userRepository.findByEmail(email);
        Pageable pageable = pageRequestDTO.toPageRequest();
        Page<WishList> wishLists = wishListRepository.findByUser(user, pageable);
        return wishLists.map(WishListConverter::convertToDTO);
    }

    public void addProductToWishList(String email, Long productId) {
        User user = userRepository.findByEmail(email);
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        WishList newWishList = new WishList(null, user, product);
        wishListRepository.save(newWishList);
    }

    @Transactional
    public void removeProductFromWishList(String email, Long productId) {
        User user = userRepository.findByEmail(email);
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        WishList wishList = wishListRepository.findByUserAndProduct(user, product)
            .orElseThrow(() -> new IllegalArgumentException("Wishlist not found for user: " + email));

        wishListRepository.delete(wishList);
    }
    public boolean isProductInWishList(String email, Long productId) {
        User user = userRepository.findByEmail(email);
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        return wishListRepository.findByUserAndProduct(user, product).isPresent();
    }
}