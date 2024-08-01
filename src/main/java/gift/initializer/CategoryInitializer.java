package gift.initializer;

import gift.model.Category;
import gift.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Arrays;
import java.util.List;

@Component
public class CategoryInitializer {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void init() {
        List<String> categoryNames = Arrays.asList(
            "교환권", "상품권", "뷰티", "패션", "식품", "리빙/도서",
            "레저/스포츠", "아티스트/캐릭터", "유아동/반려", "디지털/가전",
            "카카오프렌즈", "트렌드 선물", "백화점"
        );

        for (String categoryName : categoryNames) {
            if (!categoryRepository.existsByCategoryName(categoryName)) {
                categoryRepository.save(new Category(categoryName));
            }
        }
    }
}
