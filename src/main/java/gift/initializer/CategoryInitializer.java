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
        List<Category> categories = Arrays.asList(
            new Category("교환권", "RED", "http://example.com/coupon.jpg", "교환권 관련 카테고리"),
            new Category("상품권", "INDIGO", "http://example.com/voucher.jpg", "상품권 관련 카테고리"),
            new Category("뷰티", "YELLOW", "http://example.com/beauty.jpg", "뷰티 관련 카테고리"),
            new Category("패션", "GREEN", "http://example.com/fashion.jpg", "패션 관련 카테고리"),
            new Category("식품", "BLUE", "http://example.com/food.jpg", "식품 관련 카테고리"),
            new Category("와인/양주/전통주", "NAVY", "http://example.com/alchol.jpg", "와인/양주/전통주 관련 카테고리"),
            new Category("리빙/도서", "PURPLE", "http://example.com/book.jpg", "리빙/도서 관련 카테고리"),
            new Category("레저/스포츠", "BROWN", "http://example.com/sports.jpg", "레저/스포츠 관련 카테고리"),
            new Category("아티스트/캐릭터", "PINK", "http://example.com/artist.jpg", "아티스트/캐릭터 관련 카테고리"),
            new Category("유아동/반려", "WHITE", "http://example.com/pet.jpg", "유아동/반려 관련 카테고리"),
            new Category("디지털/가전", "BLACK", "http://example.com/digital.jpg", "디지털/가전 관련 카테고리"),
            new Category("카카오프렌즈", "ORANGE", "http://example.com/friends.jpg", "카카오프렌즈 관련 카테고리"),
            new Category("트렌드 선물", "CYAN", "http://example.com/trend.jpg", "트렌드 선물 관련 카테고리"),
            new Category("백화점", "GRAY", "http://example.com/department.jpg", "백화점 관련 카테고리")
        );

        for (Category category : categories) {
            if (!categoryRepository.existsByName(category.getName())) {
                categoryRepository.save(category);
            }
        }
    }
}
