package gift.main.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;

import java.util.Objects;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OptionRequest(
        @NotEmpty
        @Size(max = 50)
        @Pattern(regexp = "^[가-힣a-zA-Z0-9 ()\\[\\]\\+\\-&/_]*$")
        String name,

        @Min(1)
        @Max(100000000)
        int quantity) {

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptionRequest that = (OptionRequest) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
