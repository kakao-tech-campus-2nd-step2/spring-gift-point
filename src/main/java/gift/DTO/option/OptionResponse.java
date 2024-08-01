package gift.DTO.option;

import gift.domain.Option;

public class OptionResponse {

    private Long id;
    private String name;
    private Long quantity;

    protected OptionResponse(){
    }

    public OptionResponse(Long id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public static OptionResponse fromEntity(Option option) {
        return new OptionResponse(
            option.getId(),
            option.getName(),
            option.getQuantity()
        );
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }
}
