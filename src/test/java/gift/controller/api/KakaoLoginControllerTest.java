package gift.controller.api;

import gift.service.KakaoApiService;
import gift.service.MemberService;
import gift.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KakaoLoginController.class)
@AutoConfigureRestDocs
class KakaoLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;
    @MockBean
    private MemberService memberService;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private KakaoApiService kakaoApiService;

    @Test
    void redirect() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/kakaologin"))
                .andExpect(status().isFound())
                .andDo(document("kakaologin"));
    }
}