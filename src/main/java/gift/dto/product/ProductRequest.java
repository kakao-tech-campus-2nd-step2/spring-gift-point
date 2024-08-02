package gift.dto.product;

import gift.dto.option.OptionRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class ProductRequest {
    @Setter
    @NotEmpty
    @Size(max = 15)
    @Pattern(regexp = "^[a-zA-Z0-9()\\[\\]+\\-&/_가-힣]+$", message = "Invalid characters in name")
    private String name;

    @Setter
    @NotNull
    private Integer price;

    @NotEmpty
    private String imageUrl;

    @NotNull
    private Long categoryId;

    @NotNull
    private List<OptionRequest> options;

}
