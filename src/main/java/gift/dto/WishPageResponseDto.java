package gift.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class WishPageResponseDto {
    private List<WishResponseDto> wishes;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    public WishPageResponseDto(List<WishResponseDto> wishes, int currentPage, int totalPages, long totalItems) {
        this.wishes = wishes;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public static WishPageResponseDto fromPage(Page<WishResponseDto> page) {
        return new WishPageResponseDto(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    public List<WishResponseDto> getWishes() {
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
