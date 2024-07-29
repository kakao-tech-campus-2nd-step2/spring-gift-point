package gift.controller.dto;

import gift.domain.Category;
import gift.domain.Option;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public class ProductResponse {
    private Long id;
    @NotEmpty(message = "Product name cannot be empty")
    @Pattern(
        regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-&/_]{1,15}$",
        message = "상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있다 해당 특수문자 사용가능 : ( ), [ ], +, -, &, /, _"
    )
    private String name;
    private Double price;

    private String imageUrl;

    private Category category;

    private List<Option> optionList;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, Double price, String imageUrl, Category category,
        List<Option> optionList) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.optionList = optionList;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }
}
