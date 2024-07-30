package gift.dto;

import org.springframework.data.domain.Sort;

public class PageRequestDTO {
    private int page;
    private int size;
    private Sort sort;

    public PageRequestDTO(int page, String sortBy, String sortOrder) {
        this.page = page;
        this.size = 10;
        this.sort = getSort(sortBy, sortOrder);
    }

    public Sort getSort(String sortBy, String sortOrder){
        Sort sort = Sort.by(sortBy);
        if(sortOrder.equals("desc")){
            return sort.descending();
        }
        return sort;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public Sort getSort() {
        return sort;
    }
}
