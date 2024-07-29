package gift.wish.service;

import gift.product.domain.Product;
import gift.product.repository.ProductRepository;
import gift.product.service.ProductService;
import gift.user.domain.User;
import gift.user.repository.UserRepository;
import gift.wish.domain.WishlistRequest;
import gift.wish.domain.WishlistItem;
import gift.wish.domain.WishlistResponse;
import gift.wish.repository.WishlistRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductService productService;

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, ProductService productService,
        UserRepository userRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productService = productService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Page<WishlistItem> getWishlistByUserId(Long userId, Pageable pageable) {
        return wishlistRepository.findListByUserId(userId, pageable);
    }
    public Page<WishlistResponse> getWishlistResponse(Pageable pageable) {
        Page<WishlistItem> wishlistItems = wishlistRepository.findAll(pageable);
        List<WishlistResponse> wishlistResponses = wishlistItems.stream()
            .map(this::convertEntityToResponse)
            .toList();

        return new PageImpl<>(wishlistResponses, pageable, wishlistItems.getTotalElements());
    }
    public Page<WishlistResponse> getWishlistResponseByUserId(Long userId, Pageable pageable){
        Page<WishlistItem> wishlistItems = wishlistRepository.findListByUserId(userId, pageable);

        List<WishlistResponse> wishlistResponses = wishlistItems.stream()
            .map(this::convertEntityToResponse)
            .toList();

        return new PageImpl<>(wishlistResponses, pageable, wishlistItems.getTotalElements());
    }
    public void deleteById(Long wishId) {
        wishlistRepository.deleteById(wishId);
    }
    public void deleteByUserIdProductId(Long userId, Long productId){
        wishlistRepository.deleteByUserIdProductId(userId, productId);
    }
    public WishlistResponse addWish(WishlistRequest wishlistRequest) {
        User user = userRepository.findById(wishlistRequest.getUserId())
            .orElseThrow(() -> new IllegalArgumentException(
                "userId " + wishlistRequest.getUserId() + "가 없습니다."));
        Product product = productRepository.findById(wishlistRequest.getProductId())
            .orElseThrow(() -> new IllegalArgumentException(
                "productId " + wishlistRequest.getProductId() + "가 없습니다."));
        WishlistItem wishlistItem = wishlistRepository.save(new WishlistItem(user, product, wishlistRequest.getAmount()));
        return convertEntityToResponse(wishlistItem);
    }
    public void addWishes(List<WishlistRequest> wishlistRequests){
        List<WishlistItem> wishlistItems = convertRequestsToEntities(wishlistRequests);
        wishlistRepository.saveAll(wishlistItems);
    }

    private List<WishlistItem> convertRequestsToEntities(List<WishlistRequest> wishlistRequests) {
        List<WishlistItem> wishlistItemList = new ArrayList<>();
        for(WishlistRequest wishlistRequest : wishlistRequests){
            WishlistItem wishlistItem = new WishlistItem();
            wishlistItem.setUser(userRepository.findById(wishlistRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("userId " + wishlistRequest.getUserId() + "가 없습니다.")));

            Long productId = wishlistRequest.getProductId();
            wishlistItem.setProduct(productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("productId " + productId + "가 없습니다.")));

            wishlistItem.setAmount(wishlistRequest.getAmount());
            wishlistItemList.add(wishlistItem);
        }
        return wishlistItemList;
    }

    private WishlistResponse convertEntityToResponse(WishlistItem wishlistItem) {
        return new WishlistResponse(wishlistItem.getId(), wishlistItem.getUser().getId(),
            productService.convertToDTO(wishlistItem.getProduct()), wishlistItem.getAmount(), wishlistItem.getCreatedDate());
    }

}
