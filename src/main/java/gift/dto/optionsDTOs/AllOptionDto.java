package gift.dto.optionsDTOs;

import gift.model.entity.Option;

public class AllOptionDto {
    private Long id;
    private Long productId;
    private String optionName;
    private Long quantity;

    public AllOptionDto(Long id, Long productId, String optionName, Long quantity) {
        this.id = id;
        this.productId = productId;
        this.optionName = optionName;
        this.quantity = quantity;
    }

    public static AllOptionDto getAllOptionDto(Option option) {
        return new AllOptionDto(option.getId(), option.getProductID(), option.getName(), option.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getOptionName() {
        return optionName;
    }

    public Long getQuantity() {
        return quantity;
    }
}
