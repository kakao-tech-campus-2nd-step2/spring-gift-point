package gift.Initializer;

import gift.Entity.*;
import gift.Repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final WishRepository wishRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(ProductRepository productRepository, UserRepository userRepository, CategoryRepository categoryRepository, OptionRepository optionRepository, OrderRepository orderRepository, WishRepository wishRepository, PasswordEncoder passwordEncoder) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.wishRepository = wishRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Product 초기 데이터 삽입
        ProductEntity product1 = new ProductEntity("아이스 카페 아메리카노 T", 4500, "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg");
        ProductEntity product2 = new ProductEntity("따뜻한 카페 라떼 T", 5000, "https://img.danawa.com/prod_img/500000/059/749/img/13749059_1.jpg?_v=20220524145210");
        ProductEntity product3 = new ProductEntity("콜드 브루 T", 4800, "https://img.danawa.com/prod_img/500000/658/896/img/17896658_1.jpg?_v=20220923092758");
        ProductEntity product4 = new ProductEntity("콜라", 500, "https://img.danawa.com/prod_img/500000/658/896/img/17896658_1.jpg?_v=20220923092758");
        ProductEntity product5 = new ProductEntity("초콜릿", 1000, "https://img.danawa.com/prod_img/500000/658/896/img/17896658_1.jpg?_v=20220923092758");
        ProductEntity product6 = new ProductEntity("우유", 900, "https://img.danawa.com/prod_img/500000/658/896/img/17896658_1.jpg?_v=20220923092758");

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
        productRepository.save(product5);
        productRepository.save(product6);

        // User 초기 데이터 삽입
        UserEntity user1 = new UserEntity("user1@naver.com", passwordEncoder.encode("password1"));
        UserEntity user2 = new UserEntity("user2@naver.com", passwordEncoder.encode("password2"));
        UserEntity user3 = new UserEntity("user3@naver.com", passwordEncoder.encode("password3"));

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        // Category 초기 데이터 삽입
        CategoryEntity category1 = new CategoryEntity(
                "Test1",
                "https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fgift%2Fhome%2Ftheme%2F20240214_EWQEQ.png",
                "졸업을 축하하는 축하 리스트",
                "Test1",
                "#B67352"
        );

        CategoryEntity category2 = new CategoryEntity(
                "Test2",
                "https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240213090444_1b3dc970aec54eabbf3fbb7d35c0b7af.jpg",
                "졸업을 축하하는 축하 리스트",
                "Test2",
                "#B99352"
        );

        CategoryEntity category3 = new CategoryEntity(
                "Test3",
                "https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fgift%2Fhome%2Ftheme%2F292620240221_MLFJR.jpeg",
                "졸업을 축하하는 축하 리스트",
                "Test3",
                "#B67344"
        );

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        // Option 초기 데이터 삽입
        OptionEntity option1_1 = new OptionEntity("option1_1", 10L, product1);
        OptionEntity option1_2 = new OptionEntity("option1_2", 20L, product1);
        OptionEntity option1_3 = new OptionEntity("option1_3", 100L, product1);

        OptionEntity option2_1 = new OptionEntity("option2_1", 100L, product2);
        OptionEntity option2_2 = new OptionEntity("option2_2", 100L, product2);

        OptionEntity option3_1 = new OptionEntity("option3_1", 100L, product3);
        OptionEntity option3_2 = new OptionEntity("option3_2", 100L, product3);

        optionRepository.save(option1_1);
        optionRepository.save(option1_2);
        optionRepository.save(option1_3);

        optionRepository.save(option2_1);
        optionRepository.save(option2_2);

        optionRepository.save(option3_1);
        optionRepository.save(option3_2);

        // Order 초기 데이터 삽입
        OrderEntity order1 = new OrderEntity(product1, option1_1, 1L);
        OrderEntity order2 = new OrderEntity(product2, option2_1, 1L);
        OrderEntity order3 = new OrderEntity(product3, option3_1, 1L);
        OrderEntity order4 = new OrderEntity(product3, option3_1, 1L);
        OrderEntity order5 = new OrderEntity(product3, option3_1, 1L);
        OrderEntity order6 = new OrderEntity(product3, option3_1, 1L);

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);
        orderRepository.save(order5);
        orderRepository.save(order6);

        WishEntity wish1 = new WishEntity(user1, product1);
        WishEntity wish2 = new WishEntity(user2, product2);
        WishEntity wish3 = new WishEntity(user3, product3);

        wishRepository.save(wish1);
        wishRepository.save(wish2);
        wishRepository.save(wish3);

    }
}
