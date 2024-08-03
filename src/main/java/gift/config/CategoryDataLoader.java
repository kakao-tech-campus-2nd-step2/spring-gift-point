package gift.config;

import gift.dto.CategoryRequest;
import gift.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryDataLoader {

    @Bean
    public CommandLineRunner loadCategoryData(CategoryService categoryService) {
        return args -> {
            categoryService.save(new CategoryRequest("교환권", "RED", "http://example.com/coupon.jpg", "교환권 관련 카테고리"));
            categoryService.save(new CategoryRequest("상품권", "INDIGO", "http://example.com/voucher.jpg", "상품권 관련 카테고리"));
            categoryService.save(new CategoryRequest("뷰티", "YELLOW", "http://example.com/beauty.jpg", "뷰티 관련 카테고리"));
            categoryService.save(new CategoryRequest("패션", "GREEN", "http://example.com/fashion.jpg", "패션 관련 카테고리"));
            categoryService.save(new CategoryRequest("식품", "BLUE", "http://example.com/food.jpg", "식품 관련 카테고리"));
            categoryService.save(new CategoryRequest("와인/양주/전통주", "NAVY", "http://example.com/alchol.jpg", "와인/양주/전통주 관련 카테고리"));
            categoryService.save(new CategoryRequest("리빙/도서", "PURPLE", "http://example.com/book.jpg", "리빙/도서 관련 카테고리"));
            categoryService.save(new CategoryRequest("레저/스포츠", "BROWN", "http://example.com/sports.jpg", "레저/스포츠 관련 카테고리"));
            categoryService.save(new CategoryRequest("아티스트/캐릭터", "PINK", "http://example.com/artist.jpg", "아티스트/캐릭터 관련 카테고리"));
            categoryService.save(new CategoryRequest("유아동/반려", "WHITE", "http://example.com/pet.jpg", "유아동/반려 관련 카테고리"));
            categoryService.save(new CategoryRequest("디지털/가전", "BLACK", "http://example.com/digital.jpg", "디지털/가전 관련 카테고리"));
            categoryService.save(new CategoryRequest("카카오프렌즈", "ORANGE", "http://example.com/friends.jpg", "카카오프렌즈 관련 카테고리"));
            categoryService.save(new CategoryRequest("트렌드 선물", "CYAN", "http://example.com/trend.jpg", "트렌드 선물 관련 카테고리"));
            categoryService.save(new CategoryRequest("백화점", "GRAY", "http://example.com/department.jpg", "백화점 관련 카테고리"));
        };
    }
}
