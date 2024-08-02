package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.administrator.category.Category;
import gift.administrator.category.CategoryRepository;
import gift.administrator.option.Option;
import gift.administrator.option.OptionRepository;
import gift.administrator.product.Product;
import gift.administrator.product.ProductRepository;
import gift.users.user.User;
import gift.users.user.UserRepository;
import gift.users.wishlist.WishList;
import gift.users.wishlist.WishListRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@DataJpaTest
public class WishListRepositoryTest {

    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OptionRepository optionRepository;
    private Category category;
    private Product product;
    private User user;
    private Option option;
    private List<Option> options;

    @BeforeEach
    void beforeEach() {
        category = new Category("상품권", null, null, null);
        categoryRepository.save(category);
        option = new Option("XL", 3, product);
        options = new ArrayList<>(List.of(option));
        product = new Product("이춘식", 1000, "image.jpg", category, options);
        option.setProduct(product);
        productRepository.save(product);
        optionRepository.save(option);
        user = new User("admin@email.com", "1234", "local");
        userRepository.save(user);
    }

    @Test
    @DisplayName("위시리스트 추가")
    void save() {
        //Given
        WishList wishList = new WishList(user, product, 3, option);
        WishList expected = new WishList(user, product, 3, option);

        //When
        WishList actual = wishListRepository.save(wishList);

        //Then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual)
            .extracting(wish -> wish.getUser().getEmail(), wish -> wish.getProduct().getName(),
                wish -> wish.getProduct().getPrice(), wish -> wish.getProduct().getImageUrl(),
                wish -> wish.getProduct().getCategory(), WishList::getQuantity,
                wish -> wish.getOption().getName(), wish -> wish.getOption().getProduct())
            .containsExactly(expected.getUser().getEmail(), expected.getProduct().getName(),
                expected.getProduct().getPrice(), expected.getProduct().getImageUrl(),
                expected.getProduct().getCategory(), expected.getQuantity(),
                expected.getOption().getName(), expected.getOption().getProduct());
    }

    @Test
    @DisplayName("유저 아이디 위시리스트 전체 찾기")
    void findAllByUserId() {
        //Given
        Product product2 = new Product("라이언", 3000, "example.jpg", category, options);
        productRepository.save(product2);
        option.setProduct(product2);
        optionRepository.save(option);
        WishList wishList = new WishList(user, product, 3, option);
        WishList wishList1 = new WishList(user, product2, 5, option);
        wishListRepository.save(wishList);
        wishListRepository.save(wishList1);

        int page = 0;
        int size = 10;
        String sortBy = "id";
        Sort.Direction direction = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        List<WishList> expected = List.of(wishList, wishList1);

        //When
        Page<WishList> result = wishListRepository.findAllByUserId(user.getId(), pageable);

        //Then
        assertThat(result.getTotalElements()).isEqualTo(expected.size());
        assertThat(result.getContent().get(0))
            .extracting(WishList::getUser, WishList::getOption, WishList::getProduct, WishList::getQuantity)
            .containsExactly(expected.getFirst().getUser(), expected.getFirst().getOption(),
                expected.getFirst().getProduct(), expected.getFirst().getQuantity());
        assertThat(result.getContent().get(1))
            .extracting(WishList::getUser, WishList::getOption, WishList::getProduct, WishList::getQuantity)
            .containsExactly(expected.get(1).getUser(), expected.get(1).getOption(),
                expected.get(1).getProduct(), expected.get(1).getQuantity());
    }

    @Test
    @DisplayName("위시리스트 아이디로 찾기")
    void findById() {
        //Given
        WishList wishList = new WishList(user, product, 3, option);
        wishListRepository.save(wishList);
        WishList expected = new WishList(user, product, 3, option);

        //When
        Optional<WishList> actual = wishListRepository.findById(wishList.getId());

        //Then
        assertThat(actual).isPresent();
        assertThat(actual.get())
            .extracting(WishList::getUser, WishList::getProduct, WishList::getOption)
            .containsExactly(expected.getUser(), expected.getProduct(), expected.getOption());
    }

    @Test
    @DisplayName("위시리스트 아이디와 유저 아이디로 존재 여부 확인 시 존재함")
    void existsByIdAndUserId() {
        //Given
        WishList wishList = new WishList(user, product, 3, option);
        wishListRepository.save(wishList);

        //When
        boolean actual = wishListRepository.existsByIdAndUserId(wishList.getId(), user.getId());

        //Then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("위시리스트 아이디와 유저 아이디로 존재 여부 확인 시 존재하지 않음")
    void notExistsByIdAndUserId() {
        //Given
        WishList wishList = new WishList(user, product, 3, option);
        wishListRepository.save(wishList);

        //When
        boolean actual = wishListRepository.existsByIdAndUserId(wishList.getId() + 1,
            user.getId());

        //Then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("유저 아이디, 상품 아이디, 옵션 아이디로 위시리스트 존재 여부 확인 시 존재함")
    void existsByUserIdAndProductIdAndOptionId() {
        //Given
        WishList wishList = new WishList(user, product, 3, option);
        wishListRepository.save(wishList);

        //When
        boolean actual = wishListRepository.existsByUserIdAndProductIdAndOptionId(user.getId(),
            product.getId(), option.getId());

        //Then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("유저 아이디, 상품 아이디, 옵션 아이디로 위시리스트 존재 여부 확인 시 존재하지 않음")
    void notExistsByUserIdAndProductIdAndOptionId() {
        //Given
        WishList wishList = new WishList(user, product, 3, option);
        wishListRepository.save(wishList);

        //When
        boolean actual = wishListRepository.existsByUserIdAndProductIdAndOptionId(user.getId(),
            product.getId(), option.getId() + 1);

        //Then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("유저 아이디, 상품 아이디, 옵션 아이디로 위시리스트 존재하며 위시리스트 아이디로 존재하지 않음")
    void existsByUserIdAndProductIdAndOptionIdAndIdNot() {
        //Given
        WishList wishList = new WishList(user, product, 3, option);
        wishListRepository.save(wishList);

        //When
        boolean actual = wishListRepository.existsByUserIdAndProductIdAndOptionIdAndIdNot(user.getId(),
            product.getId(), option.getId(), wishList.getId() + 1);

        //Then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("유저 아이디, 상품 아이디, 옵션 아이디로 위시리스트 존재하지 않거나 위시리스트 아이디로 존재함")
    void existsByUserIdAndProductIdAndOptionIdAndIdNotReturnFalse() {
        //Given
        WishList wishList = new WishList(user, product, 3, option);
        wishListRepository.save(wishList);

        //When
        boolean actual = wishListRepository.existsByUserIdAndProductIdAndOptionIdAndIdNot(user.getId(),
            product.getId(), option.getId(), wishList.getId());

        //Then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("유저 아이디, 상품 아이디, 옵션 아이디로 위시리스트 삭제하기")
    void deleteByUserIdAndProductIdAndOptionId() {
        //Given
        WishList wishList = new WishList(user, product, 3, option);
        wishListRepository.save(wishList);

        //When
        wishListRepository.deleteByUserIdAndProductIdAndOptionId(user.getId(), product.getId(),
            option.getId());
        Optional<WishList> actual = wishListRepository.findById(wishList.getId());

        //Then
        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("위시리스트 아이디로 위시리스트 삭제하기")
    void deleteById() {
        //Given
        WishList wishList = new WishList(user, product, 3, option);
        wishListRepository.save(wishList);

        //When
        wishListRepository.deleteById(wishList.getId());
        Optional<WishList> actual = wishListRepository.findById(wishList.getId());

        //Then
        assertThat(actual).isNotPresent();
    }
}
