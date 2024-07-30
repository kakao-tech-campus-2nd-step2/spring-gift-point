package gift.initializer;

import gift.entity.Category;
import gift.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test") // 'test' 프로파일이 아닌 경우에만 실행
public class CategoryDataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategoryDataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            List<Category> categories = Arrays.asList(
                    new Category("교환권", "#FF0000", "https://example.com/exchange.png", "교환권 설명"),
                    new Category("상품권", "#00FF00", "https://example.com/gift.png", "상품권 설명"),
                    new Category("뷰티", "#0000FF", "https://example.com/beauty.png", "뷰티 설명"),
                    new Category("패션", "#FF00FF", "https://example.com/fashion.png", "패션 설명"),
                    new Category("식품", "#FFFF00", "https://example.com/food.png", "식품 설명"),
                    new Category("리빙/도서", "#00FFFF", "https://example.com/living.png", "리빙/도서 설명"),
                    new Category("레저/스포츠", "#FF00AA", "https://example.com/leisure.png", "레저/스포츠 설명"),
                    new Category("아티스트/캐릭터", "#AA00FF", "https://example.com/artist.png", "아티스트/캐릭터 설명"),
                    new Category("유아동/반려", "#55FF00", "https://example.com/kids.png", "유아동/반려 설명"),
                    new Category("디지털/가전", "#0055FF", "https://example.com/digital.png", "디지털/가전 설명"),
                    new Category("카카오프렌즈", "#FF5500", "https://example.com/kakao.png", "카카오프렌즈 설명"),
                    new Category("트렌드 선물", "#AAFF00", "https://example.com/trend.png", "트렌드 선물 설명"),
                    new Category("백화점", "#FFAA00", "https://example.com/department.png", "백화점 설명")
            );
            categoryRepository.saveAll(categories);
        }
    }
}
