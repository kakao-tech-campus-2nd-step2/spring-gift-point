package gift.config;

import gift.entity.Category;
import gift.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CategoryDatabaseInitializer implements CommandLineRunner {

    private final gift.repository.CategoryRepository CategoryRepository;

    @Autowired
    public CategoryDatabaseInitializer(CategoryRepository CategoryRepository) {
        this.CategoryRepository = CategoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Category category = new Category("기타", "#777777", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "디폴트 카테고리 입니다.");
        CategoryRepository.save(category);
    }
}