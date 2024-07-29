package gift.global.dataLoader;

import gift.domain.member.Member;
import gift.domain.cartItem.CartItem;
import gift.domain.cartItem.JpaCartItemRepository;
import gift.domain.category.Category;
import gift.domain.category.JpaCategoryRepository;
import gift.domain.option.JpaOptionRepository;
import gift.domain.option.Option;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import gift.domain.member.JpaMemberRepository;
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
    private final JpaCartItemRepository jpaCartItemRepository;
    private final JpaOptionRepository jpaOptionRepository;

    @Autowired
    public DataLoader(
        JpaProductRepository jpaProductRepository,
        JpaMemberRepository jpaMemberRepository,
        JpaCartItemRepository jpaCartItemRepository,
        JpaCategoryRepository jpaCategoryRepository,
        JpaOptionRepository jpaOptionRepository
    ) {
        this.jpaProductRepository = jpaProductRepository;
        this.jpaMemberRepository = jpaMemberRepository;
        this.jpaCartItemRepository = jpaCartItemRepository;
        this.jpaCategoryRepository = jpaCategoryRepository;
        this.jpaOptionRepository = jpaOptionRepository;
    }

    @PostConstruct
    public void init() {
        // Category
        Category ethiopia = jpaCategoryRepository.saveAndFlush(
            new Category("에티오피아산", "에티오피아 산 원두를 사용했습니다."));
        Category jamaica = jpaCategoryRepository.saveAndFlush(
            new Category("자메이카산", "자메이카산 원두를 사용했습니다."));

        // Product
        Product americano = new Product("아이스 아메리카노 T", ethiopia, 4500,
            "https://example.com/image.jpg");
        Product cafuchino = new Product("아이스 카푸치노 M", jamaica, 4700,
            "https://example.com/image.jpg");
        Product malcha = new Product("핫 말차라떼 L", ethiopia, 6800,
            "https://example.com/image.jpg");
        jpaProductRepository.save(americano);
        jpaProductRepository.save(cafuchino);
        jpaProductRepository.save(malcha);

        // dummy Product data
        for (int i = 0; i < 100; i++) {
            Product dummyProduct = new Product(
                "더미 커피 " + (i + 1),
                ethiopia,
                1000 + (i * 10),
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
        jpaCartItemRepository.save(new CartItem(minji, malcha));
        jpaCartItemRepository.save(new CartItem(junseo, cafuchino));
        jpaCartItemRepository.save(new CartItem(donghyun, cafuchino));

    }
}