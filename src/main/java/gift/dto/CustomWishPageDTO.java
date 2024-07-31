package gift.dto;

import java.util.List;

public class CustomWishPageDTO {
    private List<WishDTO> wishes;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    public CustomWishPageDTO(List<WishDTO> wishes, int currentPage, int totalPages, long totalItems) {
        this.wishes = wishes;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public List<WishDTO> getWishes() {
        return wishes;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }
}
