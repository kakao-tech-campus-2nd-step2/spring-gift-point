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
        categoryJpaRepository.save(new Category(1, "교환권", "교환권입니다.", "#FF0000", "https://www.google.com"));
        categoryJpaRepository.save(new Category(2, "상품권", "상품권입니다.", "#00FF00", "https://www.google.com"));
        categoryJpaRepository.save(new Category(3, "뷰티", "뷰티입니다.", "#0000FF", "https://www.google.com"));
        categoryJpaRepository.save(new Category(4, "패션", "패션입니다.", "#FFFF00", "https://www.google.com"));
        categoryJpaRepository.save(new Category(5, "식품", "식품입니다.", "#00FFFF", "https://www.google.com"));
        categoryJpaRepository.save(new Category(6, "리빙/도서", "리빙/도서입니다.", "#FF00FF", "https://www.google.com"));
        categoryJpaRepository.save(new Category(7, "레저/스포츠", "레저/스포츠입니다.", "#000000", "https://www.google.com"));

    }


}
