package gift.dto;

import gift.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class ProductDTO {

    private Long id;
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9\\(\\)\\[\\]\\+\\-&/_]+$", message = "잘못된 이름입니다.")
    @Size(min = 1, max = 15, message = "잘못된 이름입니다.")
    //이 정규표현식을 만족해야지만 ok
    private String name;
    @NotNull(message = "가격을 입력해주세요")
    private Integer price;
    @NotBlank(message = "이미지 주소를 입력해주세요")
    private String imageUrl;
    @NotNull(message = "카테고리를 입력해주세요")
    private Long categoryId;

    //타임리프 사용을 위한 기본 생성자
    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public ProductDTO(Long id, String name, Integer price, String imageUrl, Long categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    //상품 모델을 DTO로 빠르게 전환
    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
    }

    //디버깅 필요 시 체크용 toString
    @Override
    public String toString() {
        return "ProductDTO{" + "id=" + id + ", name='" + name + '\'' + ", price=" + price
            + ", imageUrl='" + imageUrl + '\'' + '}';
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getCategoryId() { return categoryId; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
