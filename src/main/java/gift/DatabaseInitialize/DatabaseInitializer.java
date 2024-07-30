package gift.DatabaseInitialize;

import gift.Entity.Category;
import gift.Repository.CategoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final CategoryJpaRepository categoryJpaRepository;

    @Autowired
    public DatabaseInitializer(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        categoryJpaRepository.save(new Category(1, "교환권"));
        categoryJpaRepository.save(new Category(2, "상품권"));
        categoryJpaRepository.save(new Category(3, "뷰티"));
        categoryJpaRepository.save(new Category(4, "패션"));
        categoryJpaRepository.save(new Category(5, "식품"));
        categoryJpaRepository.save(new Category(6, "리빙/도서"));
        categoryJpaRepository.save(new Category(7, "레저/스포츠"));

    }


}
