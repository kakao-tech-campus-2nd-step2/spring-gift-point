package gift.dto.response;

import java.util.List;
import gift.dto.ProductDto;

public class ProductPageResponse {
    
    private List<ProductDto> productList;
    private int currentPage;
    private int totalPages;
    private boolean hasPrevious;
    private boolean hasNext;

    public ProductPageResponse(){

    }

    public ProductPageResponse(List<ProductDto> productList, int currentPage,  boolean hasPrevious, int totalPages, boolean hasNext) {
        this.productList = productList;
        this.currentPage = currentPage;
        this.hasPrevious = hasPrevious;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
    }

    public List<ProductDto> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductDto> productList) {
        this.productList = productList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
    
}
