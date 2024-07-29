package gift.DTO.Kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Commerce {
    private String productName;
    private int regularPrice;

    public Commerce(String productName, int regularPrice) {
        this.productName = productName;
        this.regularPrice = regularPrice;
    }

    public String getProductName() {
        return productName;
    }

    public int getRegularPrice() {
        return regularPrice;
    }
}
