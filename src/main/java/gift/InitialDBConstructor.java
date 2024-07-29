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
            Category category1 = new Category("고기", "#0001","1","1");
            Category category2 = new Category("생선", "#0002","2","2");
            Category category3 = new Category("음료", "#0003","3","3");

            em.persist(category1);
            em.persist(category2);
            em.persist(category3);

            Product product1 = new Product("돼지",1000,"11");
            category1.addProduct(product1);

            Product product2 = new Product("소",2000,"22");
            category1.addProduct(product2);
            Product product3 = new Product("닭",3000,"3");
            category1.addProduct(product3);
            em.persist(product1);
            em.persist(product2);
            em.persist(product3);

            Option option1 = new Option("항정살", 100);
            product1.addOption(option1);

            Option option2 = new Option("삼겹살", 100);
            product1.addOption(option2);

            Option option3 = new Option("업진살", 100);
            product1.addOption(option3);

            em.persist(option1);
            em.persist(option2);
            em.persist(option3);
        }
    }

}

