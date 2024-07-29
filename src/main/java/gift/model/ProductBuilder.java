package gift.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public final class ProductBuilder {

    private Long id;
    private @NotNull(message = "상품 이름은 필수 입력 값입니다.")
    @Size(max = 15, message = "상품 이름은 최대 15자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_\\s]*$", message = "상품 이름에 허용되지 않는 문자가 포함되어 있습니다.") String name;
    private @NotNull Integer price;
    private @NotNull String imageUrl;
    private List<Wish> wishList;
    private Category category;
    private List<Option> optionList;

    private ProductBuilder() {
    }

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public ProductBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder withPrice(Integer price) {
        this.price = price;
        return this;
    }

    public ProductBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ProductBuilder withWishList(List<Wish> wishList) {
        this.wishList = wishList;
        return this;
    }

    public ProductBuilder withCategory(Category category) {
        this.category = category;
        return this;
    }

    public ProductBuilder withOptionList(List<Option> optionList) {
        this.optionList = optionList;
        return this;
    }

    public Product build() {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setImageUrl(imageUrl);
        product.setWishList(wishList);
        product.setCategory(category);
        product.setOptionList(optionList);
        return product;
    }
}
