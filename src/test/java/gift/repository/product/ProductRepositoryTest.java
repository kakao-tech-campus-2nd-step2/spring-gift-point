package gift.repository.product;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    private Product product1;

    @BeforeEach
    void setup() {
        for (int i = 1; i < 21; i++){
            Product product = new Product.Builder()
                    .name("테스트"+i)
                    .price(i)
                    .imageUrl("abc.png")
                    .build();

            if(i == 1){
                product1 = product;
            }

            productRepository.save(product);
        }
    }

    @Test
    @DisplayName("저장 테스트")
    void 저장_테스트(){
        //given
        Product product = new Product.Builder()
                .name("테스트")
                .price(123)
                .imageUrl("abc.png")
                .build();

        //when
        Product savedProduct = productRepository.save(product);

        //then
        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(product.getName()),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice()),
                () -> assertThat(savedProduct.getImageUrl()).isEqualTo(product.getImageUrl())
        );
    }

    @Test
    @DisplayName("단건 조회")
    void 단건_조회_테스트(){
        //given

        //when
        Product findProduct = productRepository.findById(product1.getId()).get();

        //then
        assertAll(
                () -> assertThat(findProduct.getId()).isNotNull(),
                () -> assertThat(findProduct.getName()).isEqualTo(product1.getName()),
                () -> assertThat(findProduct.getPrice()).isEqualTo(product1.getPrice()),
                () -> assertThat(findProduct.getImageUrl()).isEqualTo(product1.getImageUrl())
        );
    }

    @Test
    @DisplayName("전체 조회")
    void 전체_조회_테스트(){
        //given

        //when
        List<Product> products = productRepository.findAll();

        //then
        assertThat(products.size()).isEqualTo(20);
    }

    @Test
    @DisplayName("삭제 테스트")
    void 삭제_테스트(){
        //given

        //when
        productRepository.delete(product1);

        //then
        List<Product> products = productRepository.findAll();
        assertThat(products.size()).isEqualTo(19);
    }

    @Test
    @DisplayName("일대다 연관관계 지연로딩 테스트")
    void 연관관계_지연로딩_테스트(){
        //given
        Member member1 = new Member.Builder()
                .email("test1@pusan.ac.kr")
                .password("abc")
                .build();

        Member member2 = new Member.Builder()
                .email("test2@pusan.ac.kr")
                .password("abc")
                .build();

        entityManager.persist(member1);
        entityManager.persist(member2);

        Wish wish1 = new Wish.Builder()
                .member(member1)
                .product(product1)
                .count(3)
                .build();

        Wish wish2 = new Wish.Builder()
                .member(member2)
                .product(product1)
                .count(3)
                .build();

        wish1.addMember(member1);
        wish1.addProduct(product1);

        wish2.addMember(member2);
        wish2.addProduct(product1);

        entityManager.persist(wish1);
        entityManager.persist(wish2);
        entityManager.flush();
        entityManager.clear();

        //when

        //지연 로딩 이므로 연관관계 조회 안함
        productRepository.findById(product1.getId());
        entityManager.clear();

        //fetch join 을 사용했기 때문에 연관관계 한번에 조회
        Product findProduct = productRepository.findProductWithRelation(product1.getId()).get();

        //then
        assertAll(
                () -> assertThat(findProduct.getId()).isEqualTo(product1.getId()),
                () -> assertThat(findProduct.getName()).isEqualTo(product1.getName()),
                () -> assertThat(findProduct.getPrice()).isEqualTo(product1.getPrice()),
                () -> assertThat(findProduct.getWishList().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("page 기능 테스트")
    void 페이지_기능_테스트(){
        //given
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "price"));

        Page<Product> productWithPage = productRepository.findAll(pageRequest);
        List<Product> productList = productWithPage.stream().toList();

        assertAll(
                () -> assertThat(productWithPage.getTotalPages()).isEqualTo(4),
                () -> assertThat(productWithPage.getNumberOfElements()).isEqualTo(5),
                () -> assertThat(productList.get(0).getName()).isEqualTo("테스트20"),
                () -> assertThat(productList.get(0).getPrice()).isEqualTo(20)
        );
    }

}