package gift.repository;

import gift.model.Category;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wishlist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class WishlistRepositoryN1Test {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private final Pageable pageable = PageRequest.of(0, 10);

    @Test
    @Transactional
    void testNPlusOneFindAll() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            categories.add(new Category(null, "카테고리" + i));
        }
        categoryRepository.saveAll(categories);

        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            members.add(new Member(null, "kbm" + i, "kbm" + i + "@email.com", "mbk", "user"));
        }
        memberRepository.saveAll(members);

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            products.add(new Product(null, "상품" + i, "100", categories.get(i), "https://test"));
        }
        productRepository.saveAll(products);

        List<Wishlist> wishlists = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Wishlist wishlist = new Wishlist(null, members.get(i), products.get(i));
            wishlists.add(wishlist);
        }
        wishlistRepository.saveAll(wishlists);
        System.out.println("N + 1 테스트 (findAll)");
        Page<Wishlist> everyWishlist = wishlistRepository.findAll(pageable);
        assertFalse(everyWishlist.isEmpty());
    }

    @Test
    @Transactional
    void testNPlusOneFindByMember() {
        Member member = new Member(null, "kbm", "kbm@email.com", "mbk", "user");
        memberRepository.save(member);

        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            categories.add(new Category(null, "카테고리" + i));
        }
        categoryRepository.saveAll(categories);

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            products.add(new Product(null, "상품" + i, "100", categories.get(i), "https://test"));
        }
        productRepository.saveAll(products);

        List<Wishlist> wishlists = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Wishlist wishlist = new Wishlist(null, member, products.get(i));
            wishlists.add(wishlist);
        }
        wishlistRepository.saveAll(wishlists);

        System.out.println("N + 1 문제 테스트 (findByMember)");
        Page<Wishlist> memberWishlist = wishlistRepository.findByMember(member, pageable);
        assertFalse(memberWishlist.isEmpty());
    }
}
