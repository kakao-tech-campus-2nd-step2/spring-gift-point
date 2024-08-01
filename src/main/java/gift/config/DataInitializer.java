package gift.config;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.CategoryRepository;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository, MemberRepository memberRepository,
        WishRepository wishRepository, CategoryRepository categoryRepository, OptionRepository optionRepository){
        return args -> {
            Category testCategory1 = new Category("교환권","#6c95d1","description1","https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png");
            categoryRepository.save(testCategory1);

            Category testCategory2 = new Category("뷰티","color2","description2","imageUrl2");
            categoryRepository.save(testCategory2);

            Product testProduct = new Product("test",100,"url",testCategory1);
            productRepository.save(testProduct);

            Option testOption1 = new Option("option1",100,testProduct);
            optionRepository.save(testOption1);

            Option testOption2 = new Option("option2",200,testProduct);
            optionRepository.save(testOption2);
        };
    }
}
