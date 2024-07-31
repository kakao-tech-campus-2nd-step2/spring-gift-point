package gift.web.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.authentication.token.JwtProvider;
import gift.config.RestDocsConfiguration;
import gift.domain.Member;
import gift.domain.Member.Builder;
import gift.domain.vo.Email;
import gift.mock.MockLoginMemberArgumentResolver;
import gift.service.MemberService;
import gift.service.WishProductService;
import gift.web.dto.request.LoginRequest;
import gift.web.dto.request.member.CreateMemberRequest;
import gift.web.dto.request.wishproduct.UpdateWishProductRequest;
import gift.web.dto.response.LoginResponse;
import gift.web.dto.response.member.CreateMemberResponse;
import gift.web.dto.response.member.ReadMemberResponse;
import gift.web.dto.response.wishproduct.ReadAllWishProductsResponse;
import gift.web.dto.response.wishproduct.ReadWishProductResponse;
import gift.web.dto.response.wishproduct.UpdateWishProductResponse;
import io.swagger.v3.core.util.Json;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ActiveProfiles("test")
@SpringBootTest
@Import(RestDocsConfiguration.class)
@ExtendWith(RestDocumentationExtension.class)
class MemberApiControllerTest {

    private MockMvc mockMvc;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @MockBean
    private MemberService memberService;

    @MockBean
    private WishProductService wishProductService;

    private String accessToken;

    private static final String BASE_URL = "/api/members";

    @BeforeEach
    void setUp(
        final RestDocumentationContextProvider provider
    ) {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new MemberApiController(memberService, wishProductService))
            .setCustomArgumentResolvers(new MockLoginMemberArgumentResolver(), new PageableHandlerMethodArgumentResolver())
            .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
            .alwaysDo(restDocs)
            .build();

        Member member = new Builder().id(1L).name("회원01").email(Email.from("member01@gmail.com"))
            .build();
        accessToken = jwtProvider.generateToken(member).getValue();
    }

    @Test
    @DisplayName("회원 가입")
    void createMember() throws Exception {
        CreateMemberRequest request = new CreateMemberRequest("member01@gmail.com", "password01",
            "member01");

        String content = objectMapper.writeValueAsString(request);

        given(memberService.createMember(any()))
            .willReturn(new CreateMemberResponse(1L, "member01@gmail.com", "member01"));

        mockMvc
            .perform(
                post(BASE_URL + "/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isCreated())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 식별자"),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름")
                    )
                )
            );
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        LoginRequest request = new LoginRequest("member01@gmail.com", "password01");
        String content = objectMapper.writeValueAsString(request);

        given(memberService.login(any()))
            .willReturn(new LoginResponse(accessToken));

        mockMvc
            .perform(
                post(BASE_URL + "/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("Bearer Token")
                    )
                )
            );
    }

    @Test
    @DisplayName("회원 조회")
    void readMember() throws Exception {
        given(memberService.readMember(any(Long.class)))
            .willReturn(new ReadMemberResponse(1L, "member01@gmail.com", "password01", "member01"));

        mockMvc
            .perform(
                get(BASE_URL + "/{memberId}", 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 식별자"),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름")
                    )
                )
            );
    }

    @Test
    @DisplayName("위시 상품 조회")
    void readWishProduct() throws Exception {
        ReadAllWishProductsResponse response = new ReadAllWishProductsResponse(
            List.of(new ReadWishProductResponse(1L, 1L, "product01", 1000, 5, "https://via.placeholder.com/150"))
        );

        given(wishProductService.readAllWishProducts(any(Long.class), any()))
            .willReturn(response);

        mockMvc
            .perform(
                get(BASE_URL + "/wishlist")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    queryParameters(
                        parameterWithName("page").optional().description("페이지 번호"),
                        parameterWithName("size").optional().description("페이지 크기")
                    ),
                    responseFields(
                        fieldWithPath("wishlist[].id").type(JsonFieldType.NUMBER).description("위시 상품 식별자"),
                        fieldWithPath("wishlist[].productId").type(JsonFieldType.NUMBER).description("상품 식별자"),
                        fieldWithPath("wishlist[].name").type(JsonFieldType.STRING).description("상품명"),
                        fieldWithPath("wishlist[].price").type(JsonFieldType.NUMBER).description("가격"),
                        fieldWithPath("wishlist[].quantity").type(JsonFieldType.NUMBER).description("재고 수량"),
                        fieldWithPath("wishlist[].imageUrl").type(JsonFieldType.STRING).description("이미지 URL")
                    )
                )
            );
    }

    @Test
    @DisplayName("위시 상품 수정")
    void updateWishProduct() throws Exception {
        UpdateWishProductRequest request = new UpdateWishProductRequest(5);
        String content = Json.mapper().writeValueAsString(request);

        given(wishProductService.updateWishProduct(any(Long.class), any()))
            .willReturn(new UpdateWishProductResponse(1L, 1L, "product01", 1000, 5, "https://via.placeholder.com/150"));

        mockMvc
            .perform(
                put(BASE_URL + "/wishlist/{wishProductId}", 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("수량")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("위시 상품 식별자"),
                        fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 식별자"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품명"),
                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("재고 수량"),
                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("이미지 URL")
                    )
                )
            );
    }

    @Test
    @DisplayName("위시 상품 삭제")
    void deleteWishProduct() throws Exception {
        mockMvc
            .perform(
                delete(BASE_URL + "/wishlist/{wishProductId}", 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isNoContent())
            .andDo(
                restDocs.document()
            );
    }
}