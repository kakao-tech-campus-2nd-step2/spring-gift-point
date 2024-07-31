package gift.dto;

import java.util.List;

public class OrderPageResponseDto {
    private List<OrderResponseDto> orders;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    public OrderPageResponseDto(List<OrderResponseDto> orders, int currentPage, int totalPages, long totalItems) {
        this.orders = orders;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public List<OrderResponseDto> getOrders() {
        return orders;
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
