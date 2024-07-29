package gift;

import gift.domain.*;
import gift.repository.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public ApplicationRunner initializer(CategoryRepository categoryRepository) {
        return args -> {
            categoryRepository.save(new Category("교환권", "#6c95d1", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", ""));
            categoryRepository.save(new Category("상품권", "#ffcc00", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", ""));
            categoryRepository.save(new Category("뷰티", "#ff6699", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", ""));
            categoryRepository.save(new Category("패션", "#ff3366", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", ""));
            categoryRepository.save(new Category("식품", "#99cc00", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", ""));
            categoryRepository.save(new Category("리빙/도서", "#3399ff", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", ""));
            categoryRepository.save(new Category("레저/스포츠", "#9966cc", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", ""));
            categoryRepository.save(new Category("아티스트/캐릭터", "#ff6600", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", ""));
            categoryRepository.save(new Category("유아동/반려", "#ff9999", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", ""));
            categoryRepository.save(new Category("디지털/가전", "#666699", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", ""));
            categoryRepository.save(new Category("카카오프렌즈", "#ffccff", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", ""));
            categoryRepository.save(new Category("트렌드 선물", "#00cc99", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", ""));
            categoryRepository.save(new Category("백화점", "#339966", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", ""));
        };
    }
}
