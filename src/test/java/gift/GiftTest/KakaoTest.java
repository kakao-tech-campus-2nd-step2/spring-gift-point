package gift.GiftTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.controller.MemberController;
import gift.domain.Category.Category;
import gift.domain.Member.Member;
import gift.domain.Menu.Menu;
import gift.domain.Option.Option;
import gift.domain.Order.Order;
import gift.domain.Order.OrderRequest;
import gift.domain.WishList.WishList;
import gift.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

public class KakaoTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberController memberController;

    private Category category1;
    private Category category2;
    private Option option1;
    private Option option2;
    private Option option3;
    private Option option4;

    @BeforeEach
    void setUp() {
        category1 = new Category(null, "중식","dis","빨강","image.com", new LinkedList<Menu>());
        category2 = new Category(null, "일식","dis","빨강","image.com", new LinkedList<Menu>());
        categoryRepository.save(category2);
        Menu menu = new Menu("파스타", 3000, "naver.com", category1,new LinkedList<>());
        menuRepository.save(menu);

        option1 = new Option(null, "알리오올리오", 3L,menu);
        option2 = new Option(null, "토마토", 4L,menu);
        option3 = new Option(null, "매운맛", 3L,menu);
        option4 = new Option(null, "순한맛", 4L,menu);

        optionRepository.save(option1);
        optionRepository.save(option2);
        optionRepository.save(option3);
        optionRepository.save(option4);

        Member member = new Member("member1", "password1","김민지",new LinkedList<WishList>());
        memberRepository.save(member);

        WishList wishList = new WishList(member, menu,new Date());
        wishListRepository.save(wishList);
    }

    @Test
    @DisplayName("code로 Member 정보 가져오기")
    void test1() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String string = memberController.getUserAgree().getUrl();
        URI uri = new URI(string);
        ResponseEntity<Object> object = restTemplate.getForEntity(uri,Object.class);
    }
    @Test
    @DisplayName("주문하기")
    void test2() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer sampleToken");
        headers.setContentType(MediaType.APPLICATION_JSON);

        OrderRequest orderRequest = new OrderRequest(1L,1L,"문 앞에 놔주세요");

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(orderRequest), headers);

        ResponseEntity<Order> response = restTemplate.exchange(
                "/api/orders",
                HttpMethod.POST,
                entity,
                Order.class
        );
    }
}
