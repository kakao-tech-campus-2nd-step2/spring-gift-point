package gift.jpa;

import gift.option.Option;
import gift.option.OptionRepository;
import gift.product.Product;
import gift.product.ProductRepository;
import gift.user.User;
import gift.user.UserRepository;
import gift.user.UserType;
import gift.wishList.WishList;
import gift.wishList.WishListRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WishListRepositoryTest {

    @Autowired
    WishListRepository wishListRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OptionRepository optionRepository;
    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("위시리스트 저장 테스트")
    void save() {
        User user = new User("email@kakao.com",
                "passwordForTest",
                "myNickName",
                UserType.NORMAL_USER);
        userRepository.save(user);
        Product product = productRepository.findById(1L).orElseThrow();
        Option option = optionRepository.save(new Option("option1", 100L, product));

        WishList expected = new WishList(10);
        user.addWishList(expected);
        option.addWishList(expected);
        WishList actual = wishListRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUser()).isEqualTo(expected.getUser()),
                () -> assertThat(actual.getOption()).isEqualTo(expected.getOption()),
                () -> assertThat(actual.getCount()).isEqualTo(expected.getCount())
        );

    }

    @Test
    @DisplayName("사용자 ID로 위시리스트 조회 테스트")
    void findByUserID() {
        User user = new User("email@kakao.com",
                "passwordForTest",
                "myNickName",
                UserType.NORMAL_USER);
        userRepository.save(user);
        Product product = productRepository.findById(1L).orElseThrow();
        Option option = optionRepository.save(new Option("option1", 100L, product));

        User user2 = new User("email2@kakao.com",
                "passwordForTest2",
                "myNickName2",
                UserType.NORMAL_USER);
        userRepository.save(user2);
        Product product2 = productRepository.findById(2L).orElseThrow();
        Option option2 = optionRepository.save(new Option("option2", 200L, product2));


        WishList expected = wishListRepository.save(new WishList(10));
        user.addWishList(expected);
        option.addWishList(expected);

        WishList saved = wishListRepository.save(new WishList(5));
        user2.addWishList(saved);
        option2.addWishList(saved);

        List<WishList> actual = wishListRepository.findByUser(user);
        assertThat(actual.size()).isEqualTo(1);
        WishList actualWish = actual.get(0);
        assertAll(
                () -> assertThat(actualWish.getId()).isNotNull(),
                () -> assertThat(actualWish.getUser()).isEqualTo(expected.getUser()),
                () -> assertThat(actualWish.getOption()).isEqualTo(expected.getOption()),
                () -> assertThat(actualWish.getCount()).isEqualTo(expected.getCount())
        );
    }

    @Test
    @DisplayName("위시리스트의 상품 수량 변경 테스트")
    void updateCount() {
        User user = new User("email@kakao.com",
                "passwordForTest",
                "myNickName",
                UserType.NORMAL_USER);
        userRepository.save(user);
        Product product = productRepository.findById(1L).orElseThrow();
        Option option = optionRepository.save(new Option("option1", 100L, product));
        WishList wish = wishListRepository.save(new WishList(10));
        user.addWishList(wish);
        option.addWishList(wish);
        wish.setCount(100);
        WishList actual = wishListRepository.findById(wish.getId()).orElseThrow();
        assertThat(actual.getCount()).isEqualTo(100);
    }

    @Test
    @DisplayName("ID로 위시리스트 삭제 테스트")
    void deleteByID() {
        User user = new User("email@kakao.com",
                "passwordForTest",
                "myNickName",
                UserType.NORMAL_USER);
        userRepository.save(user);
        Product product = productRepository.findById(1L).orElseThrow();
        Option option = optionRepository.save(new Option("option1", 100L, product));
        WishList wish = wishListRepository.save(new WishList(10));
        
        user.addWishList(wish);
        option.addWishList(wish);
        wishListRepository.deleteById(wish.getId());
        Optional<WishList> wishList = wishListRepository.findById(wish.getId());
        assertThat(wishList.isPresent()).isFalse();
    }
}
