package gift.api.order.service;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import gift.api.option.repository.OptionRepository;
import gift.api.order.dto.OrderRequest;
import gift.global.config.KakaoProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class OrderFacadeTest {

    @Autowired
    private OrderFacade orderFacade;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private KakaoProperties properties;
    @Autowired
    private RestClient.Builder builder;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.bindTo(builder).build();
    }

    @Test
    void order() {
        // given
        var memberId = 1L;
        var optionId = 1L;
        var option = optionRepository.findById(optionId).get();
        var message = "My gift for you.";
        var orderRequest = new OrderRequest(optionId, 7, message);

        mockServer.expect(requestTo(properties.url().defaultTemplateMsgMe()))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess("{\"resultCode\":1}", MediaType.APPLICATION_JSON));

        // when
        // then
//        assertThat(orderFacade.order(memberId, orderRequest))
//            .isEqualTo(OrderResponse.of(new Order(option, message)));
    }
}