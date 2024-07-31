package gift.global.dataLoader;

import gift.domain.category.Category;
import gift.domain.category.JpaCategoryRepository;
import gift.domain.member.JpaMemberRepository;
import gift.domain.member.Member;
import gift.domain.option.JpaOptionRepository;
import gift.domain.option.Option;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import gift.domain.wish.JpaWishRepository;
import gift.domain.wish.Wish;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test") // 테스트 환경에서는 동작하지 않도록
public class DataLoader {

    private final JpaProductRepository jpaProductRepository;
    private final JpaMemberRepository jpaMemberRepository;
    private final JpaCategoryRepository jpaCategoryRepository;
    private final JpaWishRepository jpaWishRepository;
    private final JpaOptionRepository jpaOptionRepository;

    @Autowired
    public DataLoader(
        JpaProductRepository jpaProductRepository,
        JpaMemberRepository jpaMemberRepository,
        JpaWishRepository jpaWishRepository,
        JpaCategoryRepository jpaCategoryRepository,
        JpaOptionRepository jpaOptionRepository
    ) {
        this.jpaProductRepository = jpaProductRepository;
        this.jpaMemberRepository = jpaMemberRepository;
        this.jpaWishRepository = jpaWishRepository;
        this.jpaCategoryRepository = jpaCategoryRepository;
        this.jpaOptionRepository = jpaOptionRepository;
    }

    @PostConstruct
    public void init() {
        // Category
        Category birthday = jpaCategoryRepository.saveAndFlush(
            new Category("생일", "감동을 높여줄 생일 선물 리스트", "#5949A3",
                "https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fgift%2Fhome%2Ftheme%2F292020231106_MXMUB.png"));
        Category coupon = jpaCategoryRepository.saveAndFlush(
            new Category("교환권", "놓치면 후회할 교환권 특가", "#9290C3",
                "'https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240131153049_5a22b137a8d346e9beb020a7a7f4254a.jpg"));

        // Product
        Product americano = new Product("아이스 아메리카노 T", birthday, 4500, "description",
            "https://example.com/image.jpg");
        Product cafuchino = new Product("아이스 카푸치노 M", coupon, 4700,"description",
            "https://example.com/image.jpg");
        Product malcha = new Product("핫 말차라떼 L", birthday, 6800,"description",
            "https://example.com/image.jpg");
        jpaProductRepository.save(americano);
        jpaProductRepository.save(cafuchino);
        jpaProductRepository.save(malcha);

        // dummy Product data
        for (int i = 0; i < 100; i++) {
            Product dummyProduct = new Product(
                "더미 커피 " + (i + 1),
                birthday,
                1000 + (i * 10),
                "description",
                "https://example.com/dummy" + (i + 1) + ".jpg"
            );
            Product savedProduct = jpaProductRepository.save(dummyProduct);

            // dummy Option data
            jpaOptionRepository.save(new Option("option" + (i + 1), 2L, savedProduct));
        }

        // User
        Member minji = new Member("minji@example.com", "password1");
        Member junseo = new Member("junseo@example.com", "password2");
        Member donghyun = new Member("donghyun@example.com", "password3");
        jpaMemberRepository.save(minji);
        jpaMemberRepository.save(junseo);
        jpaMemberRepository.save(donghyun);

        // CartItem
        jpaWishRepository.save(new Wish(minji, malcha));
        jpaWishRepository.save(new Wish(junseo, cafuchino));
        jpaWishRepository.save(new Wish(donghyun, cafuchino));

    }
}