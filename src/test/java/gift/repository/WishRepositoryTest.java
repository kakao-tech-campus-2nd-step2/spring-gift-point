package gift.repository;

import gift.common.enums.LoginType;
import gift.exception.WishItemNotFoundException;
import gift.model.category.Category;
import gift.model.gift.Gift;
import gift.model.option.Option;
import gift.model.user.User;
import gift.model.wish.Wish;
import gift.repository.gift.GiftRepository;
import gift.repository.user.UserRepository;
import gift.repository.wish.WishRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GiftRepository giftRepository;


    private User user;
    private Gift gift;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "password", LoginType.DEFAULT);

        userRepository.save(user);

        Category category = new Category(10L, "test", "test", "test", "test");
        Option option1 = new Option("testOption", 1);
        List<Option> option = Arrays.asList(option1);

        gift = new Gift("Test Gift", 100, "test.jpg", category, option);
        giftRepository.save(gift);

        Wish wish = new Wish(user, gift, 1);
        wishRepository.save(wish);
    }

    @Test
    @DisplayName("유저정보를 통한 위시리스트 조회가 잘 되는지 테스트")
    void testFindByUser() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Wish> wishes = wishRepository.findByUser(user, pageable);

        assertThat(wishes).isNotNull();
        assertThat(wishes.getContent()).hasSize(1);
        assertThat(wishes.getContent().get(0).getGift().getName()).isEqualTo("Test Gift");
    }

    @Test
    @DisplayName("유저정보와 상품정보를 통한 위시리스트 조회가 잘 되는지 테스트")
    void testFindByUserAndGift() {
        Wish wish = wishRepository.findByUserAndGift(user, gift).orElseThrow(() -> new WishItemNotFoundException("해당 위시리스트 아이템을 찾을 수 없습니다."));

        assertThat(wish.getQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("위시리스트 삭제가 잘 되는지 테스트")
    void testDeleteByUserAndGift() {
        Wish wish = wishRepository.findByUserAndGift(user, gift).orElseThrow(() -> new WishItemNotFoundException("해당 위시리스트 아이템을 찾을 수 없습니다."));

        wishRepository.deleteByUserAndGift(user, gift);

        Optional<Wish> deletedWish = wishRepository.findByUserAndGift(user, gift);
        assertFalse(deletedWish.isPresent());
    }
}
