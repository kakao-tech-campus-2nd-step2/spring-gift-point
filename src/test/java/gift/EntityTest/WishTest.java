package gift.EntityTest;

import gift.Model.Entity.Category;
import gift.Model.Entity.Member;
import gift.Model.Entity.Product;
import gift.Model.Entity.Wish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class WishTest {

    private Category category;
    private Member member1;
    private Product product1;

    @BeforeEach
    void beforEach() {
        category = new Category("식품", "#812f3D", "식품 url", "");
        product1 = new Product("아메리카노", 4000, "아메리카노url", category);
        member1 = new Member("woo6388@naver.com", "12345678");
    }

    @Test
    void creationTest(){
        Wish wish = new Wish(member1, product1, 1);

        assertAll(
                ()->assertThat(wish.getMember()).isEqualTo(member1),
                ()-> assertThat(wish.getProduct()).isEqualTo(product1),
                ()->assertThat(wish.getCount().getValue()).isEqualTo(1)
        );
    }

    @Test
    void setterTest(){
        Wish wish = new Wish(member1, product1, 1);
        wish.update(2);

        assertAll(
                () -> assertThat(wish.getMember()).isEqualTo(member1),
                () -> assertThat(wish.getProduct()).isEqualTo(product1),
                () -> assertThat(wish.getCount().getValue()).isEqualTo(2)
        );
    }


}
