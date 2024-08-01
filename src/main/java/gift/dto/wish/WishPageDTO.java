package gift.dto.wish;

import gift.entity.WishList;
import org.springframework.data.domain.Page;

import java.util.List;

public class WishPageDTO{
    private List<ResponseWishDTO> content;
    private int number;
    private int totalElements;
    private int size;
    private boolean last;

    public WishPageDTO(Page<WishList> wishlistPage) {
        List<ResponseWishDTO> responseWishDTOs = wishlistPage
                .getContent().stream()
                .map(wishList -> wishList.toResponseDTO())
                .toList();

        this.content = responseWishDTOs;
        this.number = wishlistPage.getNumber();;
        this.totalElements = (int)wishlistPage.getTotalElements();
        this.size = wishlistPage.getSize();
        this.last = wishlistPage.isLast();
    }
}
