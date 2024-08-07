package gift.dto.wish;

import lombok.Data;

@Data
public class WishRequest {
    private Long productId;
    private int quantity;
}
