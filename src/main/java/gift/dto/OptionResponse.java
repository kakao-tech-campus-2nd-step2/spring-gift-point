package gift.dto;

import gift.entity.Option;

public class OptionResponse {

    private final Long id;
    private final String name;
    private final int quantity;

    public OptionResponse(Option option) {
        id = option.getId();
        name = option.getName();
        quantity = option.getQuantity();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
