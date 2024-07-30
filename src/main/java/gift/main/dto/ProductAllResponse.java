package gift.main.dto;

import java.util.List;

public record ProductAllResponse(
        Long id,
        String name,
        int price,
        String imageUrl,
        String seller,
        String categoryName,
        List<OptionResponse> optionResponses) {

    public ProductAllResponse(ProductResponce productResponce, List<OptionResponse> optionResponses) {
        this(
                productResponce.id(),
                productResponce.name(),
                productResponce.price(),
                productResponce.imageUrl(),
                productResponce.seller(),
                productResponce.categoryName(),
                optionResponses);
    }
}
