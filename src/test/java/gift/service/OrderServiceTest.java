package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gift.dto.memberDTO.LoginRequestDTO;
import gift.dto.memberDTO.RegisterRequestDTO;
import gift.dto.orderDTO.OrderRequestDTO;
import gift.dto.wishlistDTO.WishlistRequestDTO;
import gift.exception.InvalidInputValueException;
import gift.exception.NotFoundException;
import gift.model.Category;
import gift.model.Member;
import gift.model.Option;
import gift.model.Product;
import gift.model.Wishlist;
import gift.repository.CategoryRepository;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OptionService optionService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    private Member member;
    private Product product;
    private Product savedProduct;
    private Category category;
    private Option option;
    private Option savedOption;
    private String accessToken;

    @BeforeEach
    void setUp() {
        category = new Category(null, "임시카테고리", "#770077", "임시 이미지", "임시 설명");
        category = categoryRepository.save(category);

        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("test@email.com",
            "password");
        memberService.registerMember(registerRequestDTO);

        product = new Product(null, "테스트 상품", "100", category, "https://kakao");
        savedProduct = productRepository.save(product);

        option = new Option(null, "테스트 옵션", 10L, savedProduct);
        savedOption = optionRepository.save(option);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("test@email.com", "password");
        accessToken = memberService.loginMember(loginRequestDTO);
        member = memberService.getMemberByEmail(loginRequestDTO.email());
    }

    @Test
    void testCreateOrderWithInvalidEmail() {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO(savedOption.getId(), 5L, "메시지",
            accessToken);
        try {
            orderService.createOrder(orderRequestDTO, "invalid@kbm.com");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateOrderWithInvalidOption() {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO(-1L, 5L, "메시지", accessToken);
        try {
            orderService.createOrder(orderRequestDTO, member.getEmail());
        } catch (NotFoundException e) {
            assertThat(e).isInstanceOf(NotFoundException.class);
        }
    }

    @Test
    void testCreateOrderWithInvalidQuantity() {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO(savedOption.getId(), -5L, "메시지",
            accessToken);
        try {
            orderService.createOrder(orderRequestDTO, member.getEmail());
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    @Transactional
    void testRemoveFromWishlistByOptionId() {
        wishlistService.addWishlist(member.getEmail(), new WishlistRequestDTO(savedOption.getId()));
        wishlistService.removeWishlistByOptionId(savedOption.getId());
        List<Wishlist> wishlists = wishlistRepository.findAll();
        assertTrue(wishlists.isEmpty());
    }
}
