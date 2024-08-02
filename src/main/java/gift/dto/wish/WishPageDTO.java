package gift.dto.wish;

import gift.entity.WishList;
import org.springframework.data.domain.Page;

import java.util.List;

public class WishPageDTO {
    private List<ResponseWishDTO> content;
    private int number;
    private int totalElements;
    private int size;
    private boolean last;

    public List<ResponseWishDTO> getContent() {
        return content;
    }

    public int getNumber() {
        return number;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public int getSize() {
        return size;
    }

    public boolean isLast() {
        return last;
    }

    public WishPageDTO(Page<WishList> wishlistPage) {

        this.content = wishlistPage
                .getContent().stream()
                .map(WishList::toResponseDTO)
                .toList();
        this.number = wishlistPage.getNumber();
        this.totalElements = (int) wishlistPage.getTotalElements();
        this.size = wishlistPage.getSize();
        this.last = wishlistPage.isLast();
    }
}

