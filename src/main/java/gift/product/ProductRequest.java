package gift.product;

import gift.option.OptionRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductRequest {
    private Long id;

    @NotNull(message = "상품 명은 필수 값 입니다.")
    @Size(min = 1, max = 15, message = "상품 명의 길이는 1~15자 입니다.")
    @Pattern(regexp = "^[A-Za-z가-힣0-9()\\[\\]\\-&/_+\\s]*$", message = "( ), [ ], +, -, &, /, _ 를 제외한 특수문자는 사용할 수 없습니다.")
    private String name;

    @NotNull(message = "가격은 필수 값 입니다.")
    private int price;

    private String imageUrl;

    @NotNull(message = "카테고리는 필수 값 입니다.")
    private Long categoryId;

    @NotNull(message = "반드시 옵션 하나는 있어야 합니다.")
    private OptionRequest optionRequest;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    public OptionRequest getOptionRequest(){
        return optionRequest;
    }

    public Product toEntity(){
        return new Product(this.id, this.name, this.price, this.imageUrl, this.categoryId);
    }

}
