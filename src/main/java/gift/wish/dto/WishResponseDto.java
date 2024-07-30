package gift.wish.dto;

import gift.product.domain.ImageUrl;
import gift.product.domain.ProductName;
import gift.wish.domain.ProductCount;
import gift.wish.domain.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public record WishResponseDto(Long id, Long productId, ProductName productName,
                              ImageUrl imageUrl, ProductCount productCount) {

    public WishResponseDto(Wish wish) {
        this(wish.getId(), wish.getProduct().getId(), wish.getProduct().getName(),
                wish.getProduct().getImageUrl(), wish.getProductCount());
    }

    public static List<WishResponseDto> wishListToWishResponseList(List<Wish> wishes) {
        List<WishResponseDto> wishResponseDtos = new ArrayList<>();
        for (Wish wish : wishes) {
            wishResponseDtos.add(new WishResponseDto(wish));
        }
        return wishResponseDtos;
    }
}
