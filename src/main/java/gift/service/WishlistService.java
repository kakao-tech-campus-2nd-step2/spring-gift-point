package gift.service;

import gift.dto.Request.OptionRequest;
import gift.dto.Response.WishlistResponse;
import gift.dto.WishlistDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishlistService {
    List<WishlistDTO> getWishlistByUser(String username);
    WishlistResponse addToWishlist(String username, Long productId, int quantity, List<OptionRequest> options);
    WishlistResponse removeFromWishlist(Long id);
    WishlistResponse updateQuantity(Long id, int quantity, Long optionId);
    Page<WishlistDTO> getWishlistByUser1(String username, Pageable pageable);
    WishlistDTO getWishlistById(Long id);
    void orderWishlist(Long id);
}
