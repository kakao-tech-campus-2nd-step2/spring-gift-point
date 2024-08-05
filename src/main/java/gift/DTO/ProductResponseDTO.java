package gift.DTO;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductResponseDTO {
    private Long id;

    @Size(max = 15, message = "상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-\\&/_]*$", message = "(),[],+,-,&,/,_를 제외한 특수 문자 사용은 불가능합니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.")
    private String name;
    private int price;
    private String imageUrl;
    private Long categoryId;

    public ProductResponseDTO(Long id, String name, int price, String imageUrl, Long categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @Size(max = 15, message = "상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.") @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-\\&/_]*$", message = "(),[],+,-,&,/,_를 제외한 특수 문자 사용은 불가능합니다.") @Pattern(regexp = "^(?!.*카카오).*$", message = "카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.") String getName() {
        return name;
    }

    public void setName(@Size(max = 15, message = "상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.") @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-\\&/_]*$", message = "(),[],+,-,&,/,_를 제외한 특수 문자 사용은 불가능합니다.") @Pattern(regexp = "^(?!.*카카오).*$", message = "카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.") String name) {
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
}
