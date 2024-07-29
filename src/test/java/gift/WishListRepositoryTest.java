package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.model.Name;
import gift.model.Product;
import gift.model.User;
import gift.model.WishList;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishListRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class WishListRepositoryTest {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void saveWishList_ReturnSavedWishList() {
        // given
        User user = new User(null, "test@example.com", "password");
        userRepository.save(user);

        Product product = new Product(null, new Name("TestProduct"), 100, "http://example.com/image.png", 1L, new ArrayList<>());
        productRepository.save(product);

        WishList wishList = new WishList(null, user, product);

        // when
        WishList savedWishList = wishListRepository.save(wishList);

        // then
        assertAll(
            () -> assertThat(savedWishList.getId()).isNotNull(),
            () -> assertThat(savedWishList.getUser().getEmail()).isEqualTo(user.getEmail()),
            () -> assertThat(savedWishList.getProduct()).isEqualTo(product)
        );
    }

    @Test
    void findWishListByUser_ReturnWishList_WhenUserExists() {
        // given
        User user = new User(null, "test@example.com", "password");
        userRepository.save(user);

        Product product = new Product(null, new Name("TestProduct"), 100, "http://example.com/image.png", 1L, new ArrayList<>());
        productRepository.save(product);

        WishList wishList = new WishList(null, user, product);
        wishListRepository.save(wishList);

        // when
        List<WishList> wishLists = wishListRepository.findByUser(user);

        // then
        assertThat(wishLists).hasSize(1);
        assertThat(wishLists.get(0).getUser().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void findWishByUser_ReturnFirstPage_WithPagination() {
        // given: 유저와 5개의 위시리스트 아이템을 생성하고 저장
        User user = new User(null, "test@example.com", "password");
        userRepository.save(user);

        Product product1 = new Product(null, new Name("TestProduct1"), 101, "http://example.com/image1.png", 1L, new ArrayList<>());
        Product product2 = new Product(null, new Name("TestProduct2"), 102, "http://example.com/image2.png", 1L, new ArrayList<>());
        Product product3 = new Product(null, new Name("TestProduct3"), 103, "http://example.com/image3.png", 1L, new ArrayList<>());
        Product product4 = new Product(null, new Name("TestProduct4"), 104, "http://example.com/image4.png", 1L, new ArrayList<>());
        Product product5 = new Product(null, new Name("TestProduct5"), 105, "http://example.com/image5.png", 1L, new ArrayList<>());

        productRepository.saveAll(Arrays.asList(product1, product2, product3, product4, product5));

        wishListRepository.saveAll(Arrays.asList(
            new WishList(null, user, product1),
            new WishList(null, user, product2),
            new WishList(null, user, product3),
            new WishList(null, user, product4),
            new WishList(null, user, product5)
        ));

        // when: 첫 페이지를 요청하여 3개의 아이템을 가져옴
        Pageable pageable = PageRequest.of(0, 3);
        Page<WishList> wishListPage = wishListRepository.findByUser(user, pageable);

        // then: 전체 아이템 수가 5개이고, 첫 페이지에 3개의 아이템이 있으며, 총 페이지 수가 2개인지 검증
        assertAll(
            () -> assertThat(wishListPage.getTotalElements()).isEqualTo(5),
            () -> assertThat(wishListPage.getContent()).hasSize(3),
            () -> assertThat(wishListPage.getTotalPages()).isEqualTo(2)
        );
    }
}