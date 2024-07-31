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

    public PageRequest makeProductsPageRequest(int page, int size) {
        return PageRequest.of(page - 1, size);
    }

    public PageRequest makeWishPageRequest(int page, int size) {
        return PageRequest.of(page - 1, size);
    }

}
