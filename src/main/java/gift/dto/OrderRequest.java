package gift.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderRequest {
    private Long productId;
    private Long optionId;
    private int quantity;
    private String message;
}
