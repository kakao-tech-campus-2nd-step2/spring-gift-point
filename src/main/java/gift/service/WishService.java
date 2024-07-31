package gift.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.WishListDto;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishService{
    private final WishRepository wishRepository;
    private final ProductService productService;
    public WishService(WishRepository wishRepository, ProductService productService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
    }

    public Page<WishListDto> getWishPage(Member member, Pageable pageable) {
        Page<Wish> wishes = wishRepository.findByMember(member, pageable);
        return wishes.map(WishListDto::new);
    }

    public void addWish(Member member, Long product_id) {
        Product product = productService.getProduct(product_id);
        Wish wish = new Wish(member, product);
        wishRepository.save(wish);
    }

    public void deleteWish(Member member, Long product_id) {
        Product product = productService.getProduct(product_id);
        Wish wish = wishRepository.findByProduct(product);
        if(wish != null) {
            wish.setDeleted(true);
        }
    }
}
