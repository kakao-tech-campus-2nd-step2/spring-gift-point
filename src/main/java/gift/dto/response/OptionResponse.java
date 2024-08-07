package gift.dto.response;

import gift.domain.Option;

import java.util.List;
import java.util.stream.Collectors;

public class OptionResponse {
    private Long optionId;
    private String name;
    private Integer quantity;
    private Long productId;

    public OptionResponse(Long optionId, String name, Integer quantity, Long productId) {
        this.optionId = optionId;
        this.name = name;
        this.quantity = quantity;
        this.productId = productId;
    }

    public Long getId() {
        return optionId;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public static OptionResponse fromOption(Option option) {
        return new OptionResponse(option.getId(), option.getName(), option.getQuantity(), option.getProduct().getId());
    }

    public static List<OptionResponse> fromOptionList(List<Option> options) {
        return options.stream()
                .map(OptionResponse::fromOption)
                .collect(Collectors.toList());
    }

}