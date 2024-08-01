package gift.product;

import gift.option.OptionRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class ProductOptionRequest {
    @NotBlank(message = "상품명은 공백으로 둘 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣0-9\\(\\)\\[\\]\\+\\-&/\\_ ]{1,16}$", message = "상품명은 공백을 포함하여 최대 15자까지 입력할 수 있습니다. 특수문자는 ( ) [ ] + - & / _ 만 사용 가능합니다.")
    String name;
    @NotNull(message = "가격은 공백으로 둘 수 없습니다.")
    Long price;
    @NotBlank(message = "이미지 URL은 공백으로 둘 수 없습니다.")
    String imageUrl;

    @NotNull(message = "카테고리는 공백으로 둘 수 없습니다.")
    Long categoryId;
    @NotNull
    List<OptionRequest> options;

    public ProductOptionRequest() {
    }

    public ProductOptionRequest(String name, Long price, String imageUrl, Long categoryID, List<OptionRequest> options) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryID;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<OptionRequest> getOptions() {
        return options;
    }

    public void setOptions(List<OptionRequest> options) {
        this.options = options;
    }
}
