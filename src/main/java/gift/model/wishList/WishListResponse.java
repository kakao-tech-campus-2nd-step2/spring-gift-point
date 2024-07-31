package gift.model.wishList;

import gift.model.item.ItemDTO;

public class WishListResponse {

    private final Long wishId;
    private final ItemDTO itemDTO;

    public WishListResponse(Long id, ItemDTO itemDTO) {
        this.wishId = id;
        this.itemDTO = itemDTO;
    }

    public Long getId() {
        return wishId;
    }

    public ItemDTO getItemDTO() {
        return itemDTO;
    }
}
