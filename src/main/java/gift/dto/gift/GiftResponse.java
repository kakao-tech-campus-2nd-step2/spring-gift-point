package gift.dto.gift;

import gift.dto.category.CategoryResponse;
import gift.dto.option.OptionResponse;
import gift.model.gift.Gift;

import java.util.List;

public class GiftResponse {

    private final Long id;

    private final String name;

    private final int price;

    private final String imageUrl;

    private final CategoryResponse category;

    private final List<OptionResponse> options;


    public GiftResponse(Long id, String name, int price, String imageUrl, CategoryResponse category, List<OptionResponse> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.options = options;
    }

    public static GiftResponse from(Gift gift) {
        return new GiftResponse(gift.getId(),
                gift.getName(),
                gift.getPrice(),
                gift.getImageUrl(),
                CategoryResponse.fromEntity(gift.getCategory()),
                gift.getOptions().stream()
                        .map(OptionResponse::from)
                        .toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public List<OptionResponse> getOptions() {
        return options;
    }
}
