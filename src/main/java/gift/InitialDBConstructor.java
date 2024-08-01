package gift;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitialDBConstructor {

    private final InitDb Initdb;

    public InitialDBConstructor(InitDb Initdb) {
        this.Initdb = Initdb;
    }

    @PostConstruct
    public void initData(){
        Initdb.initData();
    }

    @Component
    @Transactional
    static class InitDb{
        private final EntityManager em;

        public InitDb(EntityManager em) {
            this.em = em;
        }

        public void initData(){
            Category category1 = new Category("고기", "#0001", "abc.png");
            Category category2 = new Category("생선", "#0002", "abc.png");
            Category category3 = new Category("음료", "#0003", "abc.png");

            em.persist(category1);
            em.persist(category2);
            em.persist(category3);

            Product product1 = new Product.Builder()
                    .name("돼지고기")
                    .category(category1)
                    .price(10000)
                    .imageUrl("abc.png")
                    .build();

            Product product2 = new Product.Builder()
                    .name("소고기")
                    .category(category1)
                    .price(10000)
                    .imageUrl("abc.png")
                    .build();

            Product product3 = new Product.Builder()
                    .name("닭고기")
                    .category(category1)
                    .price(10000)
                    .imageUrl("abc.png")
                    .build();

            em.persist(product1);
            em.persist(product2);
            em.persist(product3);

            Option option1 = new Option("항정살", 100);
            option1.addProduct(product1);

            Option option2 = new Option("삼겹살", 100);
            option2.addProduct(product1);

            Option option3 = new Option("업진살", 100);
            option3.addProduct(product1);

            em.persist(option1);
            em.persist(option2);
            em.persist(option3);

        }
    }

}
