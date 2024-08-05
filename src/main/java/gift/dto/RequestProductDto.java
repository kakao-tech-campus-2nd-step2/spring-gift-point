package gift.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RequestProductDto {

    @Size(min = 0, max = 15, message = "상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣()*\\[\\]+\\-&/_]*$", message = "( ), [ ], +, -, &, /, _ 외 특수 문자 사용 불가능합니다. ")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능합니다")
    private String name;

    private int price;

    private String imageUrl;

    private String categoryName;

    public RequestProductDto() {
    }

    public RequestProductDto(String name, int price, String imageUrl, String categoryName) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
