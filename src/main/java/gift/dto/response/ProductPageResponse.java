package gift.dto.response;

import gift.domain.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ProductPageResponse {
    private List<ProductResponse> content;
    private int number;
    private long totalElements;
    private int size;
    private boolean last;

    public ProductPageResponse(List<ProductResponse> content, int number, long totalElements, int size, boolean last) {
        this.content = content;
        this.number = number;
        this.totalElements = totalElements;
        this.size = size;
        this.last = last;
    }

    public static ProductPageResponse fromProductPage(Page<Product> products) {
        List<ProductResponse> productResponses = products.stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());

        return new ProductPageResponse(
                productResponses,
                products.getNumber(),
                products.getTotalElements(),
                products.getSize(),
                products.isLast()
        );
    }

}