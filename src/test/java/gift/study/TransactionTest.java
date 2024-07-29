package gift.study;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class TransactionService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void firstMethod() {
        System.out.println("firstMethod");
        EntityManager delegate = (EntityManager) entityManager.getDelegate();
        System.out.println("delegate = " + delegate.getTransaction().isActive());
        secondMethod();

    }

    public void secondMethod() {
        System.out.println("secondMethod");
        EntityManager delegate = (EntityManager) entityManager.getDelegate();
        System.out.println("delegate = " + delegate.getTransaction().isActive());
    }
}

@SpringBootTest
class TransactionTest {

    @Autowired
    private TransactionService transactionService;

    @Test
    @DisplayName("firstMethod")
    void firstMethod() {
        transactionService.firstMethod();
    }

    @Test
    @DisplayName("secondMethod")
    void secondMethod() {
        transactionService.secondMethod();
    }

}
