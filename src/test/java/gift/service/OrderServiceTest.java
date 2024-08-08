package gift.service;

import gift.KakaoProperties;
import gift.LoginType;
import gift.domain.*;
import gift.dto.LoginMember;
import gift.dto.request.OrderRequest;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class OrderServiceTest {

    private OptionRepository optionRepository;
    private WishRepository wishRepository;
    private MemberRepository memberRepository;
    private OrderRepository orderRepository;
    private KaKaoService kaKaoService;
    private OrderService orderService;
    private KakaoProperties properties;


    private Product product;
    private Category category;
    private Member member;
    private LoginMember loginMember;


    @BeforeEach
    void setUp() {
        optionRepository = mock(OptionRepository.class);
        wishRepository = mock(WishRepository.class);
        memberRepository = mock(MemberRepository.class);
        orderRepository = mock(OrderRepository.class);
        properties = mock(KakaoProperties.class);
        kaKaoService = new KaKaoService(properties);
        orderService = new OrderService(optionRepository, wishRepository, memberRepository, orderRepository, kaKaoService);

        // given
        product = new Product("name", 500, "image.image");
        category = new Category(1L, "상품권");
        product.setCategory(category);

        member = new Member(1L, "MemberName", "password", LoginType.EMAIL);
        member.setPoint(100);
        loginMember = new LoginMember(member.getId());

        given(memberRepository.findMemberById(any())).willReturn(Optional.of(member));
    }

    @Test
    void order_subtractOptionQuantity() {
        // given
        Option option = new Option("optionName", 100, product);
        given(optionRepository.findById(any())).willReturn(Optional.of(option));
        product.setOption(option);

        OrderRequest orderRequest = new OrderRequest(1L, 9, "Please handle this order with care.", 0);
        given(orderRepository.save(any())).willReturn(new Order(option, member, orderRequest));

        int initQuantity = option.getQuantity();


        // when
        orderService.order(loginMember, orderRequest);

        // then
        Assertions.assertThat(option.getQuantity())
                .isEqualTo(initQuantity - orderRequest.quantity());
    }

    @Test
    void order_deductPoint() {
        // given
        Option option = new Option("optionName", 100, product);
        given(optionRepository.findById(any())).willReturn(Optional.of(option));
        product.setOption(option);

        OrderRequest orderRequest = new OrderRequest(1L, 9, "Please handle this order with care.", 5);
        given(orderRepository.save(any())).willReturn(new Order(option, member, orderRequest));

        int originalPoint = member.getPoint();
        int pointToSave = (int) (product.getPrice() * orderRequest.quantity() * 0.1);


        // when
        orderService.order(loginMember, orderRequest);

        // then
        Assertions.assertThat(member.getPoint()).isEqualTo(originalPoint - orderRequest.point() + pointToSave);

    }

    @Test
    @Transactional
    void order_concurrencyTest() throws InterruptedException {
        // given
        Option option = new Option("optionName", 1, product);
        given(optionRepository.findById(any())).willReturn(Optional.of(option));
        product.setOption(option);

        OrderRequest orderRequest = new OrderRequest(option.getId(), 1, "Please handle this order with care.", 5);

        given(orderRepository.save(any())).willReturn(new Order(option, member, orderRequest));


        // when
        int numberOfThreads = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);

        Runnable task = () -> {
            try {
                System.out.println("Thread started: " + Thread.currentThread().getName());
                orderService.order(new LoginMember(member.getId()), orderRequest);
                System.out.println("Order completed successfully in thread: " + Thread.currentThread().getName());
            } catch (Exception e) {
                System.out.println("Exception in thread " + Thread.currentThread().getName() + ": " + e.getMessage());
            } finally {
                countDownLatch.countDown();
                System.out.println("Thread finished: " + Thread.currentThread().getName());
            }
        };

        executorService.execute(task);
        executorService.execute(task);
        executorService.execute(task);
        executorService.execute(task);

        countDownLatch.await();

        // then
        Assertions.assertThat(option.getQuantity()).isZero();

        executorService.shutdown();
    }

    @Test
    @Transactional
    void order_canceledOrderTest_invalidQuantity() {
        // given
        int initialPoint = member.getPoint();

        Option option = new Option("optionName", 10, product);
        int initialQuantity = option.getQuantity();
        given(optionRepository.findById(any())).willReturn(Optional.of(option));
        product.setOption(option);

        OrderRequest orderRequest = new OrderRequest(option.getId(), 100, "Please handle this order with care.", 5);

        given(orderRepository.save(any())).willReturn(new Order(option, member, orderRequest));

        // when
        assertThrows(RuntimeException.class, () -> {
            orderService.order(new LoginMember(member.getId()), orderRequest);
        });

        // then
        Assertions.assertThat(member.getPoint()).as("포인트").isEqualTo(initialPoint);
        Assertions.assertThat(option.getQuantity()).as("상품 수량").isEqualTo(initialQuantity);

    }

    @Test
    @Transactional
    void order_canceledOrderTest_invalidPoint() {
        // given
        int initialPoint = member.getPoint();

        Option option = new Option("optionName", 10, product);
        int initialQuantity = option.getQuantity();
        given(optionRepository.findById(any())).willReturn(Optional.of(option));
        product.setOption(option);

        OrderRequest orderRequest = new OrderRequest(option.getId(), 5, "Please handle this order with care.", 101);

        given(orderRepository.save(any())).willReturn(new Order(option, member, orderRequest));

        // when
        assertThrows(RuntimeException.class, () -> {
            orderService.order(new LoginMember(member.getId()), orderRequest);
        });

        // then
        Assertions.assertThat(member.getPoint()).as("포인트").isEqualTo(initialPoint);
        Assertions.assertThat(option.getQuantity()).as("상품 수량").isEqualTo(initialQuantity);

    }

}