package gift.unitTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.LoginUserArgumentResolver;
import gift.config.WebConfig;
import gift.controller.auth.AuthController;
import gift.controller.member.MemberController;
import gift.controller.member.MemberRequest;
import gift.controller.member.SignUpRequest;
import gift.domain.Grade;
import gift.service.MemberService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    @MockBean
    LoginUserArgumentResolver loginUserArgumentResolver;

    @MockBean
    private WebConfig webConfig;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void beforeAll() {
        MockedStatic<AuthController> instance = mockStatic(AuthController.class);
    }

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("회원 목록 조회 - 성공")
    @Test
    void getAllMembersTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/members")).andExpect(status().isOk());
        verify(memberService, times(1)).findAll(any(PageRequest.class));
    }

    @DisplayName("회원 조회 - 성공")
    @Test
    void getMemberTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/members/" + UUID.randomUUID()))
            .andExpect(status().isOk());
        verify(memberService, times(1)).findById(any(UUID.class));
    }

    @DisplayName("회원 가입 - 성공")
    @Test
    void signUpTest0() throws Exception {
        // given
        var request = new SignUpRequest("vaildEmail@kakao.com", "validPassword", "validNickName");

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

            // then
            .andExpect(status().isCreated());
        verify(memberService, times(1)).save(any(SignUpRequest.class));
    }

    @DisplayName("회원 가입 - 실패")
    @Test
    void signUpTest1() throws Exception {
        // given
        SignUpRequest[] requests = {new SignUpRequest(null, "validPassword", "validNickName"),
            new SignUpRequest("tooMuchLongEmail@kakao.com", "validPassword", "validNickName"),
            new SignUpRequest("invalidEmailForm", "validPassword", "validNickName"),
            new SignUpRequest("validEmail@kakao.com", "shortPw", "validNickName"),
            new SignUpRequest("validEmail@kakao.com", "validPassword", null),
            new SignUpRequest("validEmail@kakao.com", "validPassword", "tooMuchLongNickName")};
        String[] expected = {"이메일은 필수 입력 항목입니다.", "이메일은 최대 30자 이내입니다.", "적절한 이메일 형식이 아닙니다",
            "비밀번호의 길이는 8자 이상, 20자 이하 이내입니다.", "닉네임은 필수 입력 항목입니다.", "닉네임은 최대 15자까지 가능합니다."};

        // when
        for (int i = 0; i < requests.length; i++) {
            int finalIdx = i;
            mockMvc.perform(MockMvcRequestBuilders.post("/api/members/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requests[i])))
                // then
                .andExpect(status().isBadRequest()).andExpect(
                    result -> assertThat(result.getResolvedException()).isInstanceOf(
                        MethodArgumentNotValidException.class))
                .andExpect(result -> {
                    assertThat(((BindException) result.getResolvedException()).getBindingResult()
                        .getFieldError().getDefaultMessage()).isEqualTo(expected[finalIdx]);
                });
        }
        verify(memberService, times(0)).save(any(SignUpRequest.class));
    }


    @DisplayName("회원 수정 - 성공")
    @Test
    void updateMember() throws Exception {
        // given
        var request = new MemberRequest("validEmail@kakao.com", "validPassword", "validNickName",
            Grade.USER);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/members/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

            // then
            .andExpect(status().isOk());
        verify(memberService, times(1)).update(any(UUID.class), any(MemberRequest.class));
    }

    @DisplayName("회원 삭제 - 성공")
    @Test
    void deleteMember() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/members/" + UUID.randomUUID()))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));
        verify(memberService, times(1)).delete(any(UUID.class));
    }
}