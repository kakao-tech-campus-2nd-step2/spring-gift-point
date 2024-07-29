package gift.dto.request;

import gift.validation.KakaoNotAllowed;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class ProductUpdateRequest {

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

    public ProductUpdateRequest(String name, Integer price, String imageUrl, Long categoryId) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
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

}
