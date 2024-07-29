package gift.model.wishList;

import gift.model.item.ItemDTO;

public class WishListResponse {

    private final Long id;
    private final ItemDTO itemDTO;

    public WishListResponse(Long id, ItemDTO itemDTO) {
        this.id = id;
        this.itemDTO = itemDTO;
    }

    public Long getId() {
        return id;
    }

    public ItemDTO getItemDTO() {
        return itemDTO;
    }
}
