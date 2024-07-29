package gift.paging;

import gift.model.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class PagingService {

    public static final int PRODUCTS_PER_PAGE = 10;
    public static final int WISH_PER_PAGE = 5;
    public static final int SHOWING_PAGE_COUNT = 10;

    public PageRequest makeProductsPageRequest(int page, String sortOption) {
        return PageRequest.of(page - 1,
            PRODUCTS_PER_PAGE, Sort.by(Direction.ASC, sortOption));
    }

    public PageRequest makeWishPageRequest(int page, String sortOption) {
        if (sortOption.equals("id")) {
            return PageRequest.of(page - 1,
                WISH_PER_PAGE, Sort.by(Direction.ASC, sortOption));
        }
        return PageRequest.of(page - 1,
            WISH_PER_PAGE, Sort.by(Direction.ASC, "product." + sortOption));
    }

}
