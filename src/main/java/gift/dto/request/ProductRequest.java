package gift.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ProductRequest {
    
    @Size(max = 15, message = "Product name is too long!")
    @Pattern(regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-\\&\\/\\_가-힣]*$", message = "Product name has invalid character")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능합니다")
    private String productName;
    private int price;
    private String imageUrl;
    private Long categoryId;
    private List<OptionRequest> options;

    public ProductRequest(String productName, int price, String imageUrl, Long categoryId, List<OptionRequest> options) {
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.options = options;
    }


    public String getProductName(){
        return productName;
    }

    public int getPrice(){
        return price;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public Long getCategoryId(){
        return categoryId;
    }

    public List<OptionRequest> getOptions(){
        return options;
    }
}
