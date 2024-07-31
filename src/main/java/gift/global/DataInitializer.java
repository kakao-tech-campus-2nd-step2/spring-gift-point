package gift.global;

import gift.domain.entity.Category;
import gift.domain.entity.KakaoOauthMember;
import gift.domain.entity.LocalMember;
import gift.domain.entity.Member;
import gift.domain.entity.Option;
import gift.domain.entity.Order;
import gift.domain.entity.Product;
import gift.domain.entity.Wish;
import gift.domain.repository.CategoryRepository;
import gift.domain.repository.KakaoOauthMemberRepository;
import gift.domain.repository.LocalMemberRepository;
import gift.domain.repository.OptionRepository;
import gift.domain.repository.OrderRepository;
import gift.domain.repository.ProductRepository;
import gift.domain.repository.MemberRepository;
import gift.domain.repository.WishRepository;
import gift.global.WebConfig.Constants.Domain.Member.Permission;
import gift.global.WebConfig.Constants.Domain.Member.Type;
import gift.global.util.HashUtil;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataInitializer {

    @Bean
    ApplicationRunner init(
        ProductRepository product,
        MemberRepository member,
        LocalMemberRepository localMember,
        KakaoOauthMemberRepository kakaoMember,
        WishRepository wish,
        CategoryRepository category,
        OptionRepository option,
        OrderRepository order) {
        return args -> insertInitialData(product, member, localMember, kakaoMember, wish, category, option, order);
    }

    @Transactional
    public void insertInitialData(
        ProductRepository productRepository,
        MemberRepository memberRepository,
        LocalMemberRepository localMemberRepository,
        KakaoOauthMemberRepository kakaoOauthMemberRepository,
        WishRepository wishRepository,
        CategoryRepository categoryRepository,
        OptionRepository optionRepository,
        OrderRepository orderRepository
    ) {
        Category[] categories = {
            new Category("교환권", "#6c95d1", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", ""),
            new Category("식품", "#ff0000", "red.png", ""),
            new Category("리빙/도서", "#00ff00", "green.png", ""),
            new Category("디지털/가전", "#0000ff", "blue.png", "")};
        for (int i = 0; i < categories.length; i++) {
            categories[i] = categoryRepository.save(categories[i]);
        }

        Product[] products = {
            new Product("아이스 카페 아메리카노 T", 4500, "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg", categories[0]),
            new Product("제로 펩시 라임 355ml", 2300, "https://img.danawa.com/prod_img/500000/193/555/img/13555193_1.jpg?shrink=330:*&_v=20230222093241", categories[1]),
            new Product("오예스 12개입 360g", 3700, "https://img.danawa.com/prod_img/500000/965/117/img/10117965_1.jpg?shrink=330:*&_v=20191210171250", categories[1]),
            new Product("농심 육개장 사발면 소", 990, "https://i.namu.wiki/i/ydm9GPPnZldqoMbVcl-pVaodrUu6VBedp_vyZnrnn2WrYBvESNYo1BB2g7cK_w8b2Mw-C66pRScUfEJT3sIMrw.webp", categories[1]),
            new Product("바나나맛 우유 240ml", 1700, "https://img.danawa.com/prod_img/500000/107/815/img/3815107_1.jpg?_v=20231212093346", categories[1])};
        for (int i = 0; i < products.length; i++) {
            products[i] = productRepository.save(products[i]);
        }

        Option[] options = {
            new Option(products[0], "샷 추가", 500),
            new Option(products[0], "디카페인", 752),
                new Option(products[1], "온장보관", 49),
                new Option(products[1], "디카페인", 150),
            new Option(products[2], "초코크림맛", 3000),
                new Option(products[3], "나무젓가락 추가", 299920),
            new Option(products[4], "무설탕", 3400) };
        for (int i = 0; i < options.length; i++) {
            options[i] = optionRepository.save(options[i]);
        }

        Member[] members = {
            new Member("admin@example.com", Permission.ADMIN, Type.LOCAL),
            new Member("user@example.com", Permission.MEMBER, Type.LOCAL),
            new Member("user2@example.com", Permission.MEMBER, Type.LOCAL),
            new Member("kakaoUser@kakao.com", Permission.MEMBER, Type.KAKAO)};
        for(int i = 0; i < members.length; i++) {
            members[i] = memberRepository.save(members[i]);
        }

        LocalMember[] localMembers = {
            new LocalMember(HashUtil.hashCode("admin"), members[0]),
            new LocalMember(HashUtil.hashCode("user"), members[1]),
            new LocalMember(HashUtil.hashCode("user"), members[2])};
        for (int i = 0; i < localMembers.length; i++) {
            localMembers[i] = localMemberRepository.save(localMembers[i]);
        }

        KakaoOauthMember[] kakaoMembers = {
            new KakaoOauthMember(12345L, "tokenValue", members[3])};
        for (int i = 0; i < kakaoMembers.length; i++) {
            kakaoMembers[i] = kakaoOauthMemberRepository.save(kakaoMembers[i]);
        }

        Wish[] wishes = {
            new Wish(products[3], members[1], 5L),
            new Wish(products[0], members[1], 2L),
            new Wish(products[2], members[2], 4L),
            new Wish(products[4], members[2], 1L)};
        for (int i = 0; i < wishes.length; i++) {
            wishes[i] = wishRepository.save(wishes[i]);
        }

        Order[] orders = {
            new Order(members[1], options[1], 5, "경비실로 부탁드립니다."),
            new Order(members[1], options[3], 8, "안전하게 부탁드립니다."),
            new Order(members[3], options[4], 3, "빠른 배송 부탁드립니다.")
        };
        for (int i = 0; i < orders.length; i++) {
            orders[i] = orderRepository.save(orders[i]);
        }
    }
}
