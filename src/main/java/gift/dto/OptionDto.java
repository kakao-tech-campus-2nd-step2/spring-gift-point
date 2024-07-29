package gift.dto;

import gift.domain.Option;
import gift.dto.response.OptionResponse;

public class OptionDto {

    private Long id;
    private String name;
    private Long quantity;

    public OptionDto(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public OptionDto(Long id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public OptionResponse toResponseDto() {
        return new OptionResponse(this.id, this.name, this.quantity);
    }

    public Option toEntity() {
        return new Option(this.name, this.quantity);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }

}
