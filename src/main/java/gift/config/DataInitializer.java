package gift.config;

import gift.domain.model.entity.Category;
import gift.domain.model.entity.Product;
import gift.domain.model.entity.User;
import gift.domain.model.entity.Wish;
import gift.domain.repository.CategoryRepository;
import gift.domain.repository.ProductRepository;
import gift.domain.repository.UserRepository;
import gift.domain.repository.WishRepository;
import gift.util.JwtUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(ProductRepository productRepository,
        UserRepository userRepository,
        WishRepository wishRepository,
        CategoryRepository categoryRepository,
        JwtUtil jwtUtil) {
        return args -> {
            // 사용자 생성
            String email = "test123@naver.com";
            String password = "test123";
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            User user = new User(email, hashedPassword);
            User savedUser = userRepository.save(user);

            String token = jwtUtil.generateToken(email);

            // 카테고리 생성
            List<Category> categories = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                Category category = new Category("Category " + i);
                categories.add(category);
            }
            categories = categoryRepository.saveAll(categories);

            // 상품 생성
            Random random = new Random();
            List<Product> products = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                Category randomCategory = categories.get(random.nextInt(categories.size()));
                Product product = new Product(
                    "Product " + i,
                    random.nextLong(100000) + 1000,
                    "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240508101036_6c7f02cb957848a69a25018a664a3c89.jpg",
                    randomCategory
                );
                products.add(product);
            }

            products = productRepository.saveAll(products);

            // 위시리스트에 모든 상품 추가
            List<Wish> wishes = new ArrayList<>();
            for (Product product : products) {
                Wish wish = new Wish(savedUser, product);
                wishes.add(wish);
            }

            wishRepository.saveAll(wishes);

            System.out.println("사용자 생성 완료: " + savedUser.getEmail());
            System.out.println("초기 데이터 생성 완료: 100개의 상품이 추가되었습니다.");
            System.out.println("위시리스트 생성 완료: 모든 상품이 사용자의 위시리스트에 추가되었습니다.");
            System.out.println("생성된 JWT 토큰: " + token);
        };
    }
}