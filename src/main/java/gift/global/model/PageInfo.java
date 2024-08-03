package gift.global.model;

import java.util.Set;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

// resolver에서 page 정보를 받지 않고, requestbody로 받은 page dto로부터 알아서 PageRequest로 반환해주는 vo
public class PageInfo {

    // 유효한 property 검사하는 map
    private static final Set<String> PROPERTIES = Set.of("price", "product_price");

    private int pageNumber = 0;
    private int pageSize = 10;
    private Sort sort = Sort.unsorted();

    public PageInfo(Integer pageNumber, Integer pageSize,
        String sortProperty, String sortDirection) {
        setPageNumber(pageNumber);
        setPageSize(pageSize);
        setSort(sortProperty, sortDirection);
    }

    public PageRequest toPageRequest() {
        return PageRequest.of(pageNumber, pageSize, sort);
    }

    private void setPageNumber(Integer pageNumber) {
        if (pageNumber != null) {
            this.pageNumber = pageNumber;
        }
    }

    private void setPageSize(Integer pageSize) {
        if (pageSize != null) {
            this.pageSize = pageSize;
        }
    }

    private void setSort(String sortProperty, String sortDirection) {
        if (sortProperty != null && sortDirection != null) {
            // 검증 및 필요 객체 추출
            Direction direction = Direction.fromString(sortDirection);
            verifyProperty(sortProperty);

            this.sort = Sort.by(direction, sortProperty);
        }
    }

    // sortProperty가 잘못된 경우 검증
    private void verifyProperty(String property) {
        if (!PROPERTIES.contains(property)) {
            throw new IllegalArgumentException("유효하지 않은 정렬 기준입니다. (" + property + ")");
        }
    }
}
