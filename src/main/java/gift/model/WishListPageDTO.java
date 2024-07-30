package gift.model;

import java.util.List;

public record WishListPageDTO(int pageNumber, int pageSize, long totalElements, List<WishListDTO> wishlists) {

}
