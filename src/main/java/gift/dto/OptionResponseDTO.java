package gift.dto;

import gift.entity.Option;

public class OptionResponseDTO {

    private Long id;
    private String name;
    private int quantity;

    public OptionResponseDTO(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public OptionResponseDTO(Option option){
        id = option.getId();
        name= option.getName();
        quantity=option.getQuantity();
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
