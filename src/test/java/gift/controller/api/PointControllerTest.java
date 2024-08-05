package gift.controller.api;

import gift.dto.response.PointResponse;
import gift.interceptor.AuthInterceptor;
import gift.service.PointService;
import gift.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PointController.class)
@AutoConfigureRestDocs
class PointControllerTest {

    @MockBean
    private TokenService tokenService;
    @MockBean
    private PointService pointService;
    @MockBean
    private AuthInterceptor authInterceptor;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getMemberPoint() throws Exception {
        //Given
        when(authInterceptor.preHandle(any(), any(), any())).thenReturn(true);

        PointResponse pointResponse = new PointResponse(9900);
        when(pointService.getMemberPoint(any())).thenReturn(pointResponse);

        //When
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/points")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validTokenValue"))
                //Then
                .andExpectAll(
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("point").value(9900)
                )
                .andDo(document("point-get",
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),

                        requestHeaders(
                                headerWithName("Authorization").description("Authorization: Bearer ${ACCESS_TOKEN} +\n인증방식, 액세스 토큰으로 인증요청")
                        ),
                        responseFields(
                                fieldWithPath("point").type(JsonFieldType.NUMBER).description("멤버의 잔여 포인트")
                        )
                ));
    }
}
