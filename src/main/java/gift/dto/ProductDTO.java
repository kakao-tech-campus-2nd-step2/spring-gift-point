package gift.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ProductDTO {

    private Long id;

    @Valid
    @NotNull(message = "이름을 입력해주세요.")
    private NameDTO name;

    @NotNull(message = "가격을 입력해주세요.")
    private Integer price;

    @NotNull(message = "이미지 URL을 입력해주세요.")
    private String imageUrl;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Long categoryId;

    private List<OptionDTO> options = new ArrayList<>();

    public ProductDTO() {}

    public ProductDTO(Long id, NameDTO name, Integer price, String imageUrl, Long categoryId, List<OptionDTO> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.options = options;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NameDTO getName() {
        return name;
    }

    public void setName(NameDTO name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
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

    public List<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<OptionDTO> options) {
        this.options = options;
    }
}