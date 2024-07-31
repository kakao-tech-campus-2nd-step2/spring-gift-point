package gift.wishList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.wishList.service.WishProductService;

@RestController
@RequestMapping("api/v1/wishlist")
public class WishProductRestController {
	private final WishProductService wishProductService;

	@Autowired
	public WishProductRestController(WishProductService wishProductService) {
		this.wishProductService = wishProductService;
	}
}
