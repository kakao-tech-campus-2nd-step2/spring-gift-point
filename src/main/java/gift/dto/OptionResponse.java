package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.entity.Option;

public class OptionResponse {

    private Long id;
    private String name;

    @JsonProperty("stock_quantity")
    private int stockQuantity;

    public OptionResponse() {
    }

    public OptionResponse(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.stockQuantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public static OptionResponse from(Option option) {
        return new OptionResponse(option.getId(), option.getName(), option.getStockQuantity());
    }

}
