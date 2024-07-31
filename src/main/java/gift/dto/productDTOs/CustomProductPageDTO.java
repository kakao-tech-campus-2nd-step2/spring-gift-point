package gift.dto.productDTOs;

import java.util.List;

public class CustomProductPageDTO {
    private List<ProductDTO> products;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    public CustomProductPageDTO(List<ProductDTO> products, int currentPage, int totalPages, long totalItems) {
        this.products = products;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public List<ProductDTO> getProducts() {
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
