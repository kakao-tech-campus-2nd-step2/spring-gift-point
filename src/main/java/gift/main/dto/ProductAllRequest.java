package gift.main.dto;

import java.util.List;

public record ProductAllRequest(
        String name,
        int price,
        String imageUrl,
        int categoryId,
        List<OptionRequest> optionRequests) {

    public ProductAllRequest(ProductRequest productRequest,
                             OptionListRequest optionListRequest) {
        this(
                productRequest.name(),
                productRequest.price(),
                productRequest.imageUrl(),
                productRequest.categoryId(),
                optionListRequest.optionRequests()
        );
    }


}
