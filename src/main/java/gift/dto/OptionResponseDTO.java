package gift.dto;

import gift.model.Option;

public class OptionResponseDTO {

    private Long id;
    private String name;
    private int quantity;

    protected OptionResponseDTO() {}

    public OptionResponseDTO(Long id, String name, int quantity) {
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

    public int getQuantity() {
        return quantity;
    }

    public static OptionResponseDTO fromEntity(Option option) {
        return new OptionResponseDTO(option.getId(), option.getName(), option.getQuantity());
    }
}
