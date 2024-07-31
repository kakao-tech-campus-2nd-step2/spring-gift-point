package gift.dto.swagger;

import gift.dto.betweenClient.product.OneProductResponseDTO;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class GetWishListResponse extends PageImpl<OneProductResponseDTO> {
    public GetWishListResponse(List<OneProductResponseDTO> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}