package gift.wish.dto;

import gift.product.domain.ImageUrl;
import gift.product.domain.ProductName;
import gift.wish.domain.ProductCount;
import gift.wish.domain.Wish;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public record WishResponseListDto(int pageCount, List<WishResponseDto> wishResponseDtos) {
    public static WishResponseListDto wishPageToWishResponseListDto(Page<Wish> wishes) {
        List<WishResponseDto> newWishResponseDtos = new ArrayList<>();
        for (Wish wish : wishes.getContent()) {
            newWishResponseDtos.add(new WishResponseDto(wish));
        }
        return new WishResponseListDto(wishes.getTotalPages(), newWishResponseDtos);
    }
}
