package gift.repository;

import gift.dto.PageRequestDTO;
import gift.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class WishRepositoryTest {
    @Autowired
    private WishRepository wishs;

    @Autowired
    private MemberRepository members;

    @Autowired
    private ProductRepository products;

    @Autowired
    private CategoryRepository categories;

    @Autowired
    private OptionRepository optionRepository;

    @DisplayName("wish 저장")
    @Test
    void save(){
        Category category = new Category("category");
        categories.save(category);

        members.save(new Member("test.gamil.com", "test1234"));
        products.save(new Product("Product1", 1000, "1.img", category));
        Member member = members.findByEmail("test.gamil.com").orElseThrow();
        Product product = products.findByName("Product1").orElseThrow();
        Wish expected = new Wish(member, product);
        Wish actual = wishs.save(expected);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("해당 memberId를 가진 Wishlist 반환")
    @Test
    void getWishsbyMemberId(){
        Category category = new Category("category");
        categories.save(category);

        members.save(new Member("test.gamil.com", "test1234"));
        products.save(new Product("Product1", 1000, "1.img", category));
        products.save(new Product("Product2", 5000, "2.img", category));
        Member member = members.findByEmail("test.gamil.com").orElseThrow();
        Product product1 = products.findByName("Product1").orElseThrow();
        Product product2 = products.findByName("Product2").orElseThrow();
        Wish wish1 = new Wish(member, product1);
        Wish wish2 = new Wish(member, product2);
        wishs.save(wish1);
        wishs.save(wish2);
        PageRequestDTO pageRequestDTO = new PageRequestDTO(0, "id", "asc");
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize(), pageRequestDTO.getSort());
        Page<Wish> actual = wishs.findByMember_Id(1L, pageRequest);
        List<Wish> expected = List.of(wish1, wish2);
        assertThat(actual.getContent()).isEqualTo(expected);
    }
}
