package gift.domain.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class WishUpdateRequestDto {

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public WishUpdateRequestDto() {
    }

    public WishUpdateRequestDto(Integer count) {
        this.count = count;
    }
}
