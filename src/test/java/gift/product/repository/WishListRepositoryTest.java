package gift.product.repository;

import gift.product.model.Category;
import gift.product.model.Member;
import gift.product.model.Product;
import gift.product.model.Wish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class WishListRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product originProduct;
    private Member originMember;
    private Category originCategory;

    @BeforeEach
    void setUp() {
        originCategory = categoryRepository.save(new Category("교환권"));
        originProduct = productRepository.save(new Product("product", 1000, "image.url", originCategory));
        originMember = memberRepository.save(new Member("user@email.com", "1234"));
    }

    @Test
    void testRegisterWishList() {
        Wish product = new Wish(originMember, originProduct);
        wishListRepository.save(product);
    }

    @Test
    void testDeleteWishList() {
        Wish wish = wishListRepository.save(new Wish(originMember, originProduct));
        wishListRepository.deleteById(wish.getId());
    }

}
