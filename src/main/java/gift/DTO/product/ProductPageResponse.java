package gift.DTO.product;

import gift.domain.Product;
import java.util.List;
import org.springframework.data.domain.Page;

public class ProductPageResponse {

    private List<ProductResponse> content;
    private int number;
    private long totalElements;
    private int size;
    private boolean last;

    public ProductPageResponse() {
    }

    public ProductPageResponse(Page<ProductResponse> productPage) {
        this.content = productPage.getContent();
        this.number = productPage.getNumber();
        this.totalElements = productPage.getTotalElements();
        this.size = productPage.getSize();
        this.last = productPage.isLast();
    }

    public List<ProductResponse> getContent() {
        return content;
    }
}
