package gift.DTO.Product;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductRequest {
    @Size(min=1, max=15, message = "상품명은 1자 이상 15자 이하여야 합니다")
    @Pattern.List({
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]+$",
                message = "특수문자는 ( ), [ ], +, -, &, /, _ 만 허용되며, 한글, 영어, 숫자만 입력 가능합니다."),
        @Pattern(regexp = "^(?!.*카카오).*$", message = "'카카오'가 포함된 상품명은 MD와 협의 후 사용해주시길 바랍니다")
    })
    private String name;
    private int price;
    private String imageUrl;
    private String categoryName;

    private String basicOption;

    public ProductRequest(){

    }
  
    public ProductRequest(String name, int price, String imageUrl, String categoryName, String basicOption) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
        this.basicOption = basicOption;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBasicOption() {
        return basicOption;
    }

    public void setBasicOption(String basicOption) {
        this.basicOption = basicOption;
    }
}