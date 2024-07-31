package gift.product;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ProductRequest {
    Long id;
    @NotBlank(message = "상품명은 공백으로 둘 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣0-9\\(\\)\\[\\]\\+\\-&/\\_ ]{1,16}$", message = "상품명은 공백을 포함하여 최대 15자까지 입력할 수 있습니다. 특수문자는 ( ) [ ] + - & / _ 만 사용 가능합니다.")
    String name;
    @NotNull(message = "가격은 공백으로 둘 수 없습니다.")
    Long price;
    @NotBlank(message = "이미지 URL은 공백으로 둘 수 없습니다.")
    String imageUrl;

    @NotNull(message = "카테고리는 공백으로 둘 수 없습니다.")
    Long categoryId;

    @AssertTrue(message = "\"카카오\"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.")
    public boolean isNameNotContainingKakao() {
        return name == null || !name.contains("카카오");
    }

    public ProductRequest() {
    }

    public ProductRequest(ProductOptionRequest request) {
        this.name = request.name;
        this.price = request.price;
        this.imageUrl = request.imageUrl;
        this.categoryId = request.categoryId;
    }

    public ProductRequest(String name, Long price, String imageUrl, Long categoryId) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public ProductRequest(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.categoryId = product.getCategory().getId();
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
