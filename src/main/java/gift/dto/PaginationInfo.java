package gift.dto;

import gift.entity.Product;
import org.springframework.data.domain.Page;

public class PaginationInfo {
    private int currentPage;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;



    public PaginationInfo(Page<Product> productPage){
        this.currentPage = productPage.getNumber()+1;
        this.totalPages = productPage.getTotalPages();
        this.hasNext = productPage.hasNext();
        this.hasPrevious = productPage.hasPrevious();
    }

    // Getters and Setters
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

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}