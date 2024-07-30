package gift.dto;

import jakarta.validation.constraints.NotBlank;

public class OptionDto {
    @NotBlank
    String name;

    public OptionDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
