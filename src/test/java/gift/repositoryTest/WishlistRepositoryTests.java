package gift.repositoryTest;

import gift.model.*;
import gift.repository.CategoryRepository;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WishlistRepositoryTests {

    @Autowired
    private WishlistRepository wishListRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndFindWishList() {
        Category category = new Category("TestCategory");
        categoryRepository.save(category);

        Member member = new Member("jiu@gmail.com", "password123");
        memberRepository.save(member);

        Option option1 = new Option("Option1", 10);
        Option option2 = new Option("Option2", 20);

        Product product = new Product("상품1", 1000, "http://example.com/image.jpg", category, Arrays.asList(option1, option2));
        productRepository.save(product);

        Wishlist wishList = new Wishlist(member, product);
        wishListRepository.save(wishList);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Wishlist> foundWishLists = wishListRepository.findByMemberId(member.getId(), pageable);

        assertThat(foundWishLists.getContent()).hasSize(1);
    }

    @Test
    void testRemoveProductFromWishList() {
        Category category = new Category("TestCategory");
        categoryRepository.save(category);

        Member member = new Member("jiu@gmail.com", "password123");
        memberRepository.save(member);

        Option option1 = new Option("Option1", 10);
        Option option2 = new Option("Option2", 20);

        Product product = new Product("상품1", 1000, "http://example.com/image.jpg", category, Arrays.asList(option1, option2));
        productRepository.save(product);

        Wishlist wishList = new Wishlist(member, product);
        wishListRepository.save(wishList);

        wishListRepository.deleteByMemberIdAndProductId(member.getId(), product.getId());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Wishlist> foundWishLists = wishListRepository.findByMemberId(member.getId(), pageable);

        assertThat(foundWishLists.getContent()).isEmpty();
    }
}
