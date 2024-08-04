package gift.dto.option;

import gift.entity.Option;

public class OptionResponseDto {
    private Long id;
    private String name;
    private int quantity;

    public OptionResponseDto(Option option) {
        this.id = option.getId();
        this.name = option.getName();
        this.quantity = option.getQuantity();
    }

    public OptionResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
