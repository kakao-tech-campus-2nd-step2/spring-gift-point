package gift.dto.swagger;

import gift.dto.betweenClient.product.ProductResponseDTO;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class GetProduct extends PageImpl<ProductResponseDTO> {
    public GetProduct(List<ProductResponseDTO> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}
