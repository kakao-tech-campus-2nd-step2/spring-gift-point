package gift.repository;

import gift.model.member.Member;
import gift.model.product.Category;
import gift.model.product.Product;
import gift.model.product.ProductName;
import gift.model.wish.Wish;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save(){
        Member expectedMember = new Member("qwer@gmail.com","1234","root");
        Category category = new Category("category1");
        Product expectedProduct = new Product(category,new ProductName("product1"),1000,"qwer.com");
        memberRepository.save(expectedMember);
        categoryRepository.save(category);
        productRepository.save(expectedProduct);

        Wish newWish = new Wish(expectedProduct, expectedMember, 123);
        Wish savedWish = wishRepository.save(newWish);
        assertAll(
                () -> assertThat(savedWish.getMember().getId()).isNotNull(),
                () -> assertThat(savedWish.getProduct().getId()).isEqualTo(expectedProduct.getId())
        );
    }

    @Test
    void delete(){
        Member expectedMember = new Member("qwer@gmail.com","1234","root");
        memberRepository.save(expectedMember);

        Category category = new Category("category1");
        categoryRepository.save(category);

        Product expectedProduct = new Product(category,new ProductName("product1"),1000,"qwer.com");
        productRepository.save(expectedProduct);

        Wish newWish = new Wish(expectedProduct, expectedMember, 123);
        wishRepository.save(newWish);
        wishRepository.delete(newWish);
        Optional<Wish> actual = wishRepository.findById(1L);
        assertThat(actual).isEmpty();
    }

    @Test
    public void updateWishTest(){
        Member expectedMember = new Member("qwer@gmail.com","1234","root");
        memberRepository.save(expectedMember);

        Category category = new Category("category1");
        categoryRepository.save(category);

        Product expectedProduct = new Product(category,new ProductName("product1"),1000,"qwer.com");
        productRepository.save(expectedProduct);

        Wish originWish = new Wish(expectedProduct,expectedMember,1000 );
        Wish newWish = new Wish(expectedProduct, expectedMember,2000);
        originWish.updateWish(newWish);

        assertEquals(2000, originWish.getAmount());
    }
}