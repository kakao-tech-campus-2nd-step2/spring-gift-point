package gift.dto;

import gift.model.GiftOption;

public class GiftOptionResponse {

    private Long id;
    private String name;
    private Integer quantity;

    public GiftOptionResponse(GiftOption giftOption) {
        this.id = giftOption.getId();
        this.name = giftOption.getName();
        this.quantity = giftOption.getQuantity();
    }

    public GiftOptionResponse(Long id, String name, Integer quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
