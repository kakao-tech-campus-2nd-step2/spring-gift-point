package gift.domain;

import gift.entity.Product;
import gift.entity.Wishlist;

public class WishlistDTO {
    public int id;
    public Product product;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }
}
