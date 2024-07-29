package gift.wish.service;

import gift.product.service.ProductService;
import gift.wish.domain.WishlistDTO;
import gift.wish.domain.WishlistItem;
import gift.wish.domain.WishlistResponse;
import gift.wish.repository.WishlistRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductService productService;

    public WishlistService(WishlistRepository wishlistRepository, ProductService productService) {
        this.wishlistRepository = wishlistRepository;
        this.productService = productService;
    }


    public Page<WishlistItem> getWishlistByUserId(Long userId, Pageable pageable) {
        return wishlistRepository.findListByUserId(userId, pageable);
    }
    public Page<WishlistResponse> getWishlistResponseByUserId(Long userId, Pageable pageable){
        Page<WishlistItem> wishlistItems = wishlistRepository.findListByUserId(userId, pageable);

        List<WishlistResponse> wishlistResponses = wishlistItems.stream()
            .map(item -> new WishlistResponse(
                item.getId(),
                item.getUser().getId(),
                productService.convertToDTO(item.getProduct()),
                item.getAmount()))
            .toList();

        return new PageImpl<>(wishlistResponses, pageable, wishlistItems.getTotalElements());
    }
    public List<WishlistItem> saveWishlistItemsWithUserId(Long userId, List<WishlistItem> wishlistItems) {
        List<WishlistItem> userWishlistItems = wishlistRepository.findListByUserId(userId);
        if(!userWishlistItems.isEmpty()){
            Map<Long, WishlistItem> userWishlistMap = convertListToMap(userWishlistItems);
            userWishlistItems = mergeWishlist(wishlistItems, userWishlistMap);
        }else{
            userWishlistItems = wishlistItems;
        }
        return wishlistRepository.saveAll(userWishlistItems);
    }
    public void deleteByUserIdProductId(Long userId, Long productId){
        wishlistRepository.deleteByUserIdProductId(userId, productId);
    }
    private static Map<Long, WishlistItem> convertListToMap(List<WishlistItem> wishlistItems) {
        Map<Long, WishlistItem> userWishlistMap = new HashMap<>();
        for(WishlistItem wishlistItem : wishlistItems){
            userWishlistMap.put(wishlistItem.getProduct().getId(), wishlistItem);
        }
        return userWishlistMap;
    }

    private static List<WishlistItem> mergeWishlist(List<WishlistItem> wishlistItems,
        Map<Long, WishlistItem> userWishlistMap) {
        for (WishlistItem wishlistItem : wishlistItems) {
            Long productId = wishlistItem.getProduct().getId();
            if (userWishlistMap.containsKey(productId)) {
                WishlistItem existingItem = userWishlistMap.get(productId);
                existingItem.setAmount(existingItem.getAmount() + wishlistItem.getAmount());
                userWishlistMap.put(productId, existingItem);
            } else {
                userWishlistMap.put(productId, wishlistItem);
            }
        }
        return new ArrayList<>(userWishlistMap.values());
    }
}
