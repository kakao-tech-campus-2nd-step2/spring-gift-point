package gift;

import gift.domain.Category;
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
            Category category1 = new Category("고기", "#0001");
            Category category2 = new Category("생선", "#0002");
            Category category3 = new Category("음료", "#0003");

            em.persist(category1);
            em.persist(category2);
            em.persist(category3);
        }
    }

}
