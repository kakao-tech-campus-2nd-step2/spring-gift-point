package gift.domain;

import gift.entity.ProductEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

public class Product {

    private Long id;

    @NotBlank(message = "상품 이름을 입력해 주세요.")
    @Size(min = 1, max = 15)
    @Pattern.List({
        @Pattern(regexp = "^[가-힣a-zA-Z0-9()\\[\\]+\\-&/_]*$", message = "유효한 이름이 아닙니다"),
        @Pattern(regexp = "^(?!.*카카오).*$", message = "'카카오' 포함된 경우 담당 MD와 협의가 필요합니다.")
    })
    private String name;

    @NotNull(message = "상품 가격을 입력해 주세요.")
    @Positive(message = "상품의 가격은 0 이상이어야 합니다.")
    private Long price;

    @NotBlank(message = "이미지URL을 입력해 주세요.")
    @Pattern(regexp = "^(http(s?):)([/|.\\w|\\s|-])*\\.(?:jpg|gif|png)$", message = "URL 형식에 맞추어 작성해주세요")
    private String imageUrl;

//    @NotBlank(message = "카테고리를 무조건 선택해 주세요.")
    private Long categoryId;

    private List<Long> optionIds;

    public Product() {

    }

    public Product(String name, Long price, String imageUrl, Long categoryId, List<Long> optionIds) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.optionIds = optionIds;
    }

    public Product(Long id, String name, Long price, String imageUrl, Long categoryId, List<Long> optionIds) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.optionIds = optionIds;
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

    public List<Long> getOptionIds() {
        return optionIds;
    }

}

