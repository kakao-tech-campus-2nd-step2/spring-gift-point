package gift.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class ProductPageResponseDto {
    private List<ProductResponseDto> products;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    public ProductPageResponseDto(List<ProductResponseDto> products, int currentPage, int totalPages, long totalItems) {
        this.products = products;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public static ProductPageResponseDto fromPage(Page<ProductResponseDto> page) {
        return new ProductPageResponseDto(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    public List<ProductResponseDto> getProducts() {
        return products;
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
