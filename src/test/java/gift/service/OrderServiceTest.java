package gift.service;

import gift.LoginType;
import gift.domain.*;
import gift.dto.request.OrderRequest;
import gift.repository.OptionRepository;
import gift.repository.WishRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class OrderServiceTest {

    private OptionRepository optionRepository;
    private WishRepository wishRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        optionRepository = mock(OptionRepository.class);
        wishRepository = mock(WishRepository.class);
        orderService = new OrderService(optionRepository, wishRepository);
    }

    @Test
    void order_subtractOptionQuantity() {
        // given
        Product product = new Product(1L, "name", 500, "image.image");
        Category category1 = new Category(1L, "상품권");
        Option option = new Option("optionName", 100, product);
        int initQuantity = option.getQuantity();
        product.setCategory(category1);
        product.setOption(option);

        OrderRequest orderRequest = new OrderRequest(1L, 9, "Please handle this order with care.");
        Member member = new Member(1L, "MemberName", "password", LoginType.EMAIL);

        given(optionRepository.findById(any())).willReturn(Optional.of(option));

        // when
        orderService.order(member, orderRequest);

        // then
        Assertions.assertThat(option.getQuantity())
                .isEqualTo(initQuantity - orderRequest.quantity());
    }

}