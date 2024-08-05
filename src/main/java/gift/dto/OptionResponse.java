package gift.dto;

import gift.domain.Option;

public class OptionResponse {

    private Long id;
    private String name;
    private int quantity;

    protected OptionResponse() {}

    public OptionResponse(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public static OptionResponse convertToDto(Option option) {
        return new OptionResponse(option.getId(), option.getName(), option.getQuantity());
    }
}
