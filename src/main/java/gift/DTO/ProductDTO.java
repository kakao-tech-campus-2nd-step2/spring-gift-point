package gift.DTO;

import gift.Entity.OptionEntity;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public class ProductDTO {

    private Long id;

    @Size(max = 15, message = "상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-\\&/_]*$", message = "(),[],+,-,&,/,_를 제외한 특수 문자 사용은 불가능합니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.")
    private String name;
    private int price;
    private String imageUrl;

    private List<WishDTO> wishes;
    private CategoryDTO category;
    private List<OptionDTO> options;

    public ProductDTO() {}

    public void setOptions(List<OptionDTO> options) {
        this.options = options;
    }

    public List<WishDTO> getWishes() {
        return wishes;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public List<OptionDTO> getOptions() {
        return options;
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

    public void setWishes(List<WishDTO> wishes) {
        this.wishes = wishes;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }
}
