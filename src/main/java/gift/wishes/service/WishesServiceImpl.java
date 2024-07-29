package gift.wishes.service;

import gift.core.PagedDto;
import gift.core.domain.product.Product;
import gift.core.domain.product.ProductRepository;
import gift.core.domain.product.exception.ProductNotFoundException;
import gift.core.domain.user.User;
import gift.core.domain.user.UserRepository;
import gift.core.domain.user.exception.UserNotFoundException;
import gift.core.domain.wishes.WishesService;
import gift.core.domain.wishes.WishesRepository;
import gift.core.domain.wishes.exception.WishAlreadyExistsException;
import gift.core.domain.wishes.exception.WishNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class WishesServiceImpl implements WishesService {
    private final ProductRepository productRepository;
    private final WishesRepository wishesRepository;
    private final UserRepository userRepository;

    @Autowired
    public WishesServiceImpl(
            WishesRepository wishesRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.wishesRepository = wishesRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addProductToWishes(Long userId, Product product) {
        if (!productRepository.exists(product.id())) {
            throw new ProductNotFoundException();
        }
        if (wishesRepository.exists(userId, product.id())) {
            throw new WishAlreadyExistsException();
        }
        wishesRepository.saveWish(userId, product.id());
    }

    @Override
    public void removeProductFromWishes(Long userId, Product product) {
        if (!wishesRepository.exists(userId, product.id())) {
            throw new WishNotFoundException();
        }
        wishesRepository.removeWish(userId, product.id());
    }

    @Override
    public List<Product> getWishlistOfUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return List.copyOf(wishesRepository.getWishlistOfUser(user));
    }

    @Override
    public PagedDto<Product> getWishlistOfUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return wishesRepository.getWishlistOfUser(user, pageable);
    }
}
