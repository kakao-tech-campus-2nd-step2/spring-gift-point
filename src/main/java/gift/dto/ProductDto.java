package gift.dto;

import gift.entity.Product;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDto {

    private long id;

    @Size(max = 15, message = "Product name is too long!")
    @Pattern(regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-\\&\\/\\_가-힣]*$", message = "Product name has invalid character")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능합니다")
    private String name;
    private int price;
    private String imageUrl;
    private Long categoryId;
    private List<OptionDto> options;


    public ProductDto() {
    }

    @JsonCreator
    public ProductDto(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("price") int price,
        @JsonProperty("image_url") String imageUrl,
        @JsonProperty("category_id") Long categoryId,
        @JsonProperty("options") List<OptionDto> options
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.options = options;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Long getCategoryId(){
        return categoryId;
    }

    public List<OptionDto> getOptions(){
        return options;
    }

    public void setOptions(List<OptionDto> options){
        this.options = options;
    }

    public static ProductDto fromEntity(Product product) {
        return new ProductDto(
            product.getId(), 
            product.getName(), 
            product.getPrice(), 
            product.getImageUrl(), 
            product.getCategory().getId(),
            product.getOptions()
                .stream()
                .map(OptionDto::fromEntity)
                .toList());
    }
}
