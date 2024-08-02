package gift.repository;

import gift.model.member.Member;
import gift.model.product.Category;
import gift.model.product.Product;
import gift.model.product.ProductName;
import gift.model.wish.Wish;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class WishRepositoryTest {

   /* @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Mock
    private Category category;

    @Mock
    private Product product;

    @Test
    void save(){
        Member expectedMember = new Member("qwer@gmail.com","1234");
        memberRepository.save(expectedMember);
        categoryRepository.save(category);

        Wish newWish = new Wish(product, expectedMember);
        Wish savedWish = wishRepository.save(newWish);
        assertThat(savedWish.getMember().getId()).isNotNull();
    }

    @Test
    void delete(){
        Member expectedMember = new Member("qwer@gmail.com","1234");
        memberRepository.save(expectedMember);
        Product expectedProduct = new Product(category,new ProductName("product1"),1000,"qwer.com");
        productRepository.save(expectedProduct);
    }

    @Test
    public void updateWishTest(){
        Member expectedMember = new Member("qwer@gmail.com","1234");
        memberRepository.save(expectedMember);
        Product expectedProduct = new Product(category,new ProductName("product1"),1000,"qwer.com");
        productRepository.save(expectedProduct);
    }*/
}