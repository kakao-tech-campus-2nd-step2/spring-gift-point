package gift.restdocs.login;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.JwtService;
import gift.auth.JwtTokenProvider;
import gift.config.LoginWebConfig;
import gift.controller.login.LoginController;
import gift.model.Member;
import gift.request.JoinRequest;
import gift.response.JoinResponse;
import gift.restdocs.AbstractRestDocsTest;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

@WebMvcTest(value = LoginController.class,
    excludeFilters = {@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = LoginWebConfig.class)})
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class RestDocsLoginTest extends AbstractRestDocsTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private String token = "{ACCESS_TOKEN}";

    @Test
    void join() throws Exception {
        //given
        ReflectionTestUtils.setField(jwtService, "jwtTokenProvider", jwtTokenProvider);
        String email = "abc123@a.com";
        String password = "1234";
        JoinRequest joinRequest = new JoinRequest(email, password);
        String content = objectMapper.writeValueAsString(joinRequest);
        Member member = new Member(email, password);
        JoinResponse joinResponse = new JoinResponse(email, password);

        given(memberService.join(any(String.class), any(String.class)))
            .willReturn(member);
        doCallRealMethod().when(jwtService).addTokenInCookie(any(Member.class), any(
            HttpServletResponse.class));
        given(jwtTokenProvider.generateToken(any(Member.class)))
            .willReturn(token);

        //when //then
        mockMvc.perform(post("/api/members/register")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(cookie().value("access_token", token))
            .andDo(print());
    }

    @Test
    void login() throws Exception {
        //given
        ReflectionTestUtils.setField(jwtService, "jwtTokenProvider", jwtTokenProvider);
        String email = "abc123@a.com";
        String password = "1234";
        JoinRequest joinRequest = new JoinRequest(email, password);
        String content = objectMapper.writeValueAsString(joinRequest);
        Member member = new Member(email, password);
        JoinResponse joinResponse = new JoinResponse(email, password);

        given(memberService.login(any(String.class), any(String.class)))
            .willReturn(member);
        doCallRealMethod().when(jwtService).addTokenInCookie(any(Member.class), any(
            HttpServletResponse.class));
        given(jwtTokenProvider.generateToken(any(Member.class)))
            .willReturn(token);

        //when //then
        mockMvc.perform(post("/api/members/login")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(cookie().value("access_token", token))
            .andDo(print());
    }



}
