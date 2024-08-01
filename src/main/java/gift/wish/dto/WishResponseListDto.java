package gift.wish.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.wish.domain.Wish;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public record WishResponseListDto(@JsonProperty("total_page") int totalPage, @JsonProperty("content") List<WishResponseDto> wishResponseDtos) {
    public static WishResponseListDto wishPageToWishResponseListDto(Page<Wish> wishes) {
        List<WishResponseDto> newWishResponseDtos = new ArrayList<>();
        for (Wish wish : wishes.getContent()) {
            newWishResponseDtos.add(new WishResponseDto(wish));
        }
        return new WishResponseListDto(wishes.getTotalPages(), newWishResponseDtos);
    }
}
