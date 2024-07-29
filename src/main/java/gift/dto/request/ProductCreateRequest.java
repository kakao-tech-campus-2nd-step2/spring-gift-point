package gift.dto.request;

import gift.validation.KakaoNotAllowed;
import gift.validation.NoDuplicatedOptionName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public class ProductCreateRequest {

    @NotBlank
    @Length(max = 15)
    @Pattern(regexp = "[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*")
    @KakaoNotAllowed
    private String name;

    @NotNull
    private Integer price;

    @NotBlank
    private String imageUrl;

    @NotNull
    private Long categoryId;

    @NotNull
    @Size(min = 1)
    @NoDuplicatedOptionName
    @Valid
    private List<OptionCreateRequest> options;

    public ProductCreateRequest(String name, Integer price, String imageUrl, Long categoryId, List<OptionCreateRequest> options) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public List<OptionCreateRequest> getOptions() {
        return options;
    }

}
