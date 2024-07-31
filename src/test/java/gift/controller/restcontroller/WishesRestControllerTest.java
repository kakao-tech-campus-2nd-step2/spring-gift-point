package gift.controller.restcontroller;

import gift.common.annotation.LoginMember;
import gift.controller.dto.response.PagingResponse;
import gift.controller.dto.response.ProductResponse;
import gift.controller.dto.response.WishResponse;
import gift.service.WishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class WishesRestControllerTest {

    @InjectMocks
    private WishesRestController wishesRestController;

    @Mock
    private WishService wishService;
    
    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws Exception {
        HandlerMethodArgumentResolver loginMemberResolver = mock(HandlerMethodArgumentResolver.class);
        when(loginMemberResolver.supportsParameter(any()))
                .thenAnswer(invocation -> {
                    MethodParameter parameter = invocation.getArgument(0);
                    return parameter.hasParameterAnnotation(LoginMember.class);
                });
        when(loginMemberResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(1L);

        mockMvc = MockMvcBuilders.standaloneSetup(wishesRestController)
                .setCustomArgumentResolvers(loginMemberResolver, new PageableHandlerMethodArgumentResolver())
                .build();
    }



    @DisplayName("Wish 목록 조회[성공]")
    @Test
    void getWishSuccess() throws Exception {
        // given
        int dataCount = 5;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        given(wishService.findAllWishPagingByMemberId(any(), eq(pageable)))
                .willReturn(wishList(dataCount));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/wishes")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        then(wishService).should().findAllWishPagingByMemberId(any(), eq(pageable));
        resultActions.andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.size", is(size))
                ).andExpect(
                        jsonPath("$.number", is(page))
                ).andExpect(
                        jsonPath("$.totalElements", is(dataCount))
                );
    }



    private PagingResponse<WishResponse> wishList(int count) {
        List<WishResponse> wishList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            wishList.add(new WishResponse((long) i, i + 1,
                    new ProductResponse.Info((long)i, "testProduct", i * 1000,
                            "URL", " ", null, null),
                    null, null));
        }
        Page<WishResponse> pages = new PageImpl<>(wishList, PageRequest.of(0, 10), count);
        return PagingResponse.from(pages);
    }
}