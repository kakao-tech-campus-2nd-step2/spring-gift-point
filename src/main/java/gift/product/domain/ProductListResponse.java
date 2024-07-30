package gift.product.domain;

import java.util.List;

public class ProductListResponse {
    private List<ProductResponse> products;
    private int number;
    private int totalElement;
    private int size;
    private boolean last;

    public ProductListResponse(List<ProductResponse> products, int number, int totalElement, int size, boolean last) {
        this.products = products;
        this.number = number;
        this.totalElement = totalElement;
        this.size = size;
        this.last = last;
    }

    public ProductListResponse(List<ProductResponse> products) {
        this.products = products;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public int getNumber() {
        return number;
    }

    public int getTotalElement() {
        return totalElement;
    }

    public int getSize() {
        return size;
    }

    public boolean isLast() {
        return last;
    }
}
