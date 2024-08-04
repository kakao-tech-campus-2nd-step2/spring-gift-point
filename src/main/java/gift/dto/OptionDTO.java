package gift.dto;

import gift.domain.Option;

public class OptionDTO {

    private Long id;
    private String name;
    private int quantity;

    protected OptionDTO() {}

    public OptionDTO(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
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

    public static OptionDTO convertToDto(Option option) {
        return new OptionDTO(option.getId(), option.getName(), option.getQuantity());
    }
}
