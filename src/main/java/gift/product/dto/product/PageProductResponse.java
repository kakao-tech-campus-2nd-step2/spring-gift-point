package gift.product.dto.product;

import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PageProductResponse extends PageImpl<ProductResponse> {

    public PageProductResponse(List<ProductResponse> content,
        Pageable pageable,
        long total) {
        super(content, pageable, total);
    }

    public PageProductResponse(List<ProductResponse> content) {
        super(content);
    }
}
