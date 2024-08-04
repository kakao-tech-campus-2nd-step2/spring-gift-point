package gift.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ProductRequest {
    
    @Size(max = 15, message = "Product name is too long!")
    @Pattern(regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-\\&\\/\\_가-힣]*$", message = "Product name has invalid character")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능합니다")
    private String name;
    private int price;
    private String imageUrl;
    private Long categoryId;
    private List<OptionRequest> options;

    public ProductRequest(String name, int price, String imageUrl, Long categoryId, List<OptionRequest> options) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.options = options;
    }


    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice(){
        return price;
    }

    public void setPrice(int price){
        this.price = price;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCategoryId(){
        return categoryId;
    }

    public void setCategoryId(){
        this.categoryId = categoryId;
    }

    public List<OptionRequest> getOptions(){
        return options;
    }

    public void setOptions(List<OptionRequest> options){
        this.options = options;
    }
}
