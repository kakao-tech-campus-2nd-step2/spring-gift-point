package gift.dto;

import java.util.List;

public class ProductPageResponse {
  private final List<ProductDto> products;
  private final int currentPage;
  private final int totalPages;
  private final long totalItems;

  public ProductPageResponse(List<ProductDto> products, int currentPage, int totalPages, long totalItems) {
    this.products = products;
    this.currentPage = currentPage;
    this.totalPages = totalPages;
    this.totalItems = totalItems;
  }

  public List<ProductDto> getProducts() {
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