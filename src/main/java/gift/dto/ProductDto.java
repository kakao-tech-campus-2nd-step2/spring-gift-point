package gift.dto;

import gift.entity.Product;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductDto {

    private long id;

    @Size(max = 15, message = "Product name is too long!")
    @Pattern(regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-\\&\\/\\_가-힣]*$", message = "Product name has invalid character")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능합니다")
    private String name;
    private int price;
    private String imageUrl;
    private String category;

    public ProductDto() {
    }

    public ProductDto(long id, String name, int price, String imageUrl, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static ProductDto fromEntity(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getName());
    }
}
