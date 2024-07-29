package gift.user;

import gift.wishList.WishList;

public interface IntegratedUser {
    Long getId();
    public void addWishList(WishList wishList);

    public void removeWishList(WishList wishList);

    public void removeWishLists();
}
