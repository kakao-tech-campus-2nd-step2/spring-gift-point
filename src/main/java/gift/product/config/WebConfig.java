package gift.product.config;

import gift.product.JwtCookieToHeaderInterceptor;
import gift.product.TokenValidationInterceptor;
import gift.product.property.KakaoProperties;
import gift.product.repository.AuthRepository;
import gift.product.repository.CategoryRepository;
import gift.product.repository.KakaoTokenRepository;
import gift.product.repository.OptionRepository;
import gift.product.repository.OrderRepository;
import gift.product.repository.ProductRepository;
import gift.product.repository.WishRepository;
import gift.product.service.AuthService;
import gift.product.service.CategoryService;
import gift.product.service.OptionService;
import gift.product.service.OrderService;
import gift.product.service.ProductService;
import gift.product.service.WishService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ProductRepository productRepository;
    private final WishRepository wishRepository;
    private final AuthRepository authRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final KakaoTokenRepository kakaoTokenRepository;
    private final KakaoProperties kakaoProperties;


    public WebConfig(ProductRepository productRepository,
        WishRepository wishRepository,
        AuthRepository authRepository,
        CategoryRepository categoryRepository,
        OptionRepository optionRepository,
        OrderRepository orderRepository,
        KakaoTokenRepository kakaoTokenRepository,
        KakaoProperties kakaoProperties) {
        this.productRepository = productRepository;
        this.wishRepository = wishRepository;
        this.authRepository = authRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.kakaoTokenRepository = kakaoTokenRepository;
        this.kakaoProperties = kakaoProperties;
    }

    @Bean
    public ProductService productService() {
        return new ProductService(productRepository, categoryRepository, optionRepository);
    }

    @Bean
    public WishService wishService() {
        return new WishService(wishRepository, productRepository, authRepository);
    }

    @Bean
    public AuthService authService() {
        return new AuthService(authRepository, kakaoTokenRepository, kakaoProperties);
    }

    @Bean
    public CategoryService categoryService() {
        return new CategoryService(categoryRepository);
    }

    @Bean
    public OptionService optionService() {
        return new OptionService(optionRepository, productRepository);
    }

    @Bean
    public OrderService orderService() {
        return new OrderService(orderRepository,
            wishRepository,
            optionRepository,
            authRepository,
            kakaoTokenRepository);
    }

    @Bean
    public TokenValidationInterceptor tokenValidationInterceptor() {
        return new TokenValidationInterceptor(authRepository);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenValidationInterceptor())
            .order(2)
            .addPathPatterns("/admin/wishes/**")
            .addPathPatterns("/api/wishes/**")
            .addPathPatterns("/api/orders/**")
            .addPathPatterns("/api/members/login/kakao/unlink");
        registry.addInterceptor(new JwtCookieToHeaderInterceptor())
            .order(1)
            .addPathPatterns("/admin/wishes/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
            .exposedHeaders("Location")
            .allowCredentials(true)
            .maxAge(1800);
    }
}
