package gift.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import gift.dto.WishlistRequest;
import gift.dto.WishlistResponse;
import gift.entity.Product;
import gift.entity.User;
import gift.entity.Wishlist;
import gift.exception.InvalidProductException;
import gift.exception.InvalidUserException;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;

@Service
public class WishlistService {
	
	private final WishlistRepository wishlistRepository;
	private final ProductRepository productRepository;
	private final UserService userService;
	
	public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository,
			UserService userService) {
		this.wishlistRepository = wishlistRepository;
		this.productRepository = productRepository;
		this.userService = userService;
	}
	
	public Page<WishlistResponse> getWishlist(String token, Pageable pageable) {
        User user = userService.getUserFromToken(token);
        Page<Wishlist> wishlistPage = wishlistRepository.findByUserId(user.getId(), pageable);
        return wishlistPage.map(Wishlist::toDto);
	}
	
	public void addWishlist(String token, WishlistRequest request, BindingResult bindingResult) {
		validateBindingResult(bindingResult);
		User user = userService.getUserFromToken(token);
        Product product = findProductById(request.getProductId());
        Wishlist newWishlist = request.toEntity(user, product);
        wishlistRepository.save(newWishlist);
	}
	
	public void removeWishlist(String token, WishlistRequest request, BindingResult bindingResult) {
		validateBindingResult(bindingResult);
		User user = userService.getUserFromToken(token);
        Wishlist deleteWishlist = findWishlistById(request.getProductId());
        deleteWishlist.validateUserPermission(user);
        wishlistRepository.delete(deleteWishlist);
	}
	
	public void updateWishlistQuantity(String token, WishlistRequest request, BindingResult bindingResult) {
		validateBindingResult(bindingResult);
		User user = userService.getUserFromToken(token);
        Wishlist updateWishlist = findWishlistById(request.getProductId());
        updateWishlist.validateUserPermission(user);
        if (request.getQuantity() == 0) {
        	wishlistRepository.delete(updateWishlist);
        	return;
        }
        updateWishlist.setQuantity(request.getQuantity());
        wishlistRepository.save(updateWishlist);
	}
	
	private void validateBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
        	String errorMessage = bindingResult
					.getFieldError()
					.getDefaultMessage();
			throw new InvalidUserException(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }
	
	private Product findProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new InvalidProductException("The product does not exits."));
	}
	
	private Wishlist findWishlistById(Long id) {
		return wishlistRepository.findById(id)
				.orElseThrow(() -> new InvalidProductException("Wishlist item not found."));
	}
}