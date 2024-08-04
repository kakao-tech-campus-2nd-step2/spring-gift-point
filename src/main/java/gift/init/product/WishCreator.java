package gift.init.product;

import gift.domain.product.Wish.createWish;
import gift.service.product.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WishCreator {

    private WishService wishService;

    @Autowired
    public WishCreator(WishService wishService) {
        this.wishService = wishService;
    }

    public void creator() {
        wishService.createWish(1L, new createWish(1L));
        wishService.createWish(1L, new createWish(2L));
        wishService.createWish(2L, new createWish(3L));
        wishService.createWish(2L, new createWish(1L));
    }
}
