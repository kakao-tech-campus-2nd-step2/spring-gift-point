package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Category;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wishlist;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("/sql/truncateIdentity.sql")
class WishlistJpaDaoTest {

    @Autowired
    MemberJpaDao memberJpaDao;
    @Autowired
    ProductJpaDao productJpaDao;
    @Autowired
    WishlistJpaDao wishlistJpaDao;
    @Autowired
    CategoryJpaDao categoryJpaDao;


    Wishlist generateWishlist() {
        Category category = new Category("음식", "Red", "http", "description");
        categoryJpaDao.save(category);

        Member member = new Member("sgoh", "pass");
        Product product = new Product("coffee", 4500L, "http", category);
        Wishlist wishlist = new Wishlist(member, product);

        member.getWishlist().add(wishlist);
        product.getWishlist().add(wishlist);
        memberJpaDao.save(member);
        productJpaDao.save(product);

        return wishlist;
    }

    @Test
    @DisplayName("위시리스트에 상품 추가 테스트")
    void save() {
        Wishlist wish = wishlistJpaDao.save(generateWishlist());
        assertThat(wish).isNotNull();
    }

    @Test
    @DisplayName("이메일과 상품ID로 조회 테스트")
    void findByEmailAndProductId() {
        Wishlist wishlist = generateWishlist();
        wishlistJpaDao.save(wishlist);

        Assertions.assertDoesNotThrow(() -> {
            wishlistJpaDao.findByMember_EmailAndProduct_Id(wishlist.getMember().getEmail(),
                wishlist.getProduct().getId());
        });
    }

    @Test
    @DisplayName("이메일로 위시리스트 목록 조회 테스트")
    void findWishlistByEmail() {
        wishlistJpaDao.save(generateWishlist());

        List<Wishlist> wishlists = memberJpaDao.findByEmail("sgoh").get().getWishlist();
        assertThat(wishlists.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("이메일과 상품ID로 위시리스트 상품 삭제 테스트")
    void deleteByEmailAndProductId() {
        Wishlist wishlist = generateWishlist();
        wishlistJpaDao.save(wishlist);

        wishlistJpaDao.deleteByMember_EmailAndProduct_Id(wishlist.getMember().getEmail(),
            wishlist.getProduct().getId());
        assertThat(wishlistJpaDao.findAll().size()).isZero();
    }
}