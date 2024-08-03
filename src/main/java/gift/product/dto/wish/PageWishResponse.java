package gift.product.dto.wish;

import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PageWishResponse extends PageImpl<WishResponse> {

    public PageWishResponse(List<WishResponse> content,
        Pageable pageable,
        long total) {
        super(content, pageable, total);
    }

    public PageWishResponse(List<WishResponse> content) {
        super(content);
    }
}
