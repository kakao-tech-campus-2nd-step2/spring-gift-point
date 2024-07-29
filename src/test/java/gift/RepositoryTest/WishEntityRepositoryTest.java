package gift.RepositoryTest;

import gift.Model.Entity.MemberEntity;
import gift.Model.Entity.ProductEntity;
import gift.Model.Entity.WishEntity;
import gift.Model.Role;
import gift.Repository.CategoryRepository;
import gift.Repository.MemberRepository;
import gift.Repository.ProductRepository;
import gift.Repository.WishRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class WishEntityRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save(){
        MemberEntity member = new MemberEntity("testEmail","testPassword", Role.CONSUMER);
        ProductEntity product = new ProductEntity(categoryRepository.findById(1L).get(),"testName",1,"testImageUrl");
        memberRepository.save(member);
        productRepository.save(product);
        WishEntity expected = new WishEntity(member, product);
        WishEntity actual = wishRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getMember()).isEqualTo(expected.getMember())
        );
    }

    @Test
    void findByName() {
        MemberEntity member = new MemberEntity("testEmail","testPassword", Role.CONSUMER);
        ProductEntity product = new ProductEntity(categoryRepository.findById(1L).get(),"testName",1,"testImageUrl");
        memberRepository.save(member);
        productRepository.save(product);

        wishRepository.save(new WishEntity(member, product));
        WishEntity actual = wishRepository.findByMemberId(member.getId()).getFirst();

        assertThat(actual.getMember()).isEqualTo(member);
    }

    @Test
    void deleteByProductIDAndMemberId(){
        MemberEntity member = new MemberEntity("testEmail","testPassword", Role.CONSUMER);
        ProductEntity product = new ProductEntity(categoryRepository.findById(1L).get(),"testName",1,"testImageUrl");
        memberRepository.save(member);
        productRepository.save(product);
        wishRepository.save(new WishEntity(member, product));
        MemberEntity actual = wishRepository.findByMemberIdAndProductId(member.getId(), product.getId()).getMember();
        assertThat(actual).isEqualTo(member);
    }
}
