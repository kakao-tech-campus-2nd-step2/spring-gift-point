package gift.restdocs.order;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.JwtTokenProvider;
import gift.config.LoginWebConfig;
import gift.controller.OrderApiController;
import gift.request.OrderRequest;
import gift.response.OrderResponse;
import gift.restdocs.AbstractRestDocsTest;
import gift.service.OrderService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@WebMvcTest(value = OrderApiController.class,
    excludeFilters = {@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = LoginWebConfig.class)})
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class RestDocsOrderTest extends AbstractRestDocsTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtTokenProvider tokenProvider;
    @MockBean
    private OrderService orderService;

    private String token = "{ACCESS_TOKEN}";
    private String oAuthToken = "OAUTH_TOKEN";


    @Test
    void makeOrder() throws Exception {
        //given
        Long memberId = 1L;
        OrderRequest orderRequest = demoRequest(1L, 1L);
        String content = objectMapper.writeValueAsString(orderRequest);
        OrderResponse orderResponse = new OrderResponse(memberId, orderRequest.optionId(),
            orderRequest.quantity(), "2024.01.01 00:00:00", orderRequest.message());
        given(orderService.makeOrder(any(Long.class), any(String.class),
            any(Long.class), any(Long.class), any(Integer.class), any(String.class)))
            .willReturn(orderResponse);

        //when //then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/orders?access_token=" + oAuthToken)
                .header("Authorization", "Bearer " + token)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("rest-docs-order-test/make-order",
                queryParameters(
                    parameterWithName("access_token").description("kakao access token")
                )));
    }

    @Test
    void getOrder() throws Exception {
        //given
        List<OrderResponse> orderResponseList = new ArrayList<>();

        LongStream.range(1, 6)
            .forEach(i ->  orderResponseList.add(new OrderResponse(i, i,
                1, "2024.01.01 00:00:00", "주문 메시지")));

        given(orderService.getOrder(any(Long.class)))
            .willReturn(orderResponseList);

        //when //then
        mockMvc.perform(get("/api/orders")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andDo(print());
    }


    private OrderRequest demoRequest(Long productId, Long optionId) {
        return new OrderRequest(productId, optionId, 1, "주문 메시지");
    }

}
