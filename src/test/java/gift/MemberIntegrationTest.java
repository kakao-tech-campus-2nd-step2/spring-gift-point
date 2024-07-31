package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.domain.Member;
import gift.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class MemberIntegrationTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("회원가입")
    void registerMember() {
        Member member = new Member("testemail@email", "testpassword");

        Member savedMember = memberRepository.save(member);
        String memberEmail = savedMember.getEmail();

        Member registerdMember = memberRepository.findByEmail(memberEmail);
        assertThat(registerdMember.getEmail()).isEqualTo("testemail@email");
        assertThat(registerdMember.getPassword()).isEqualTo("testpassword");
    }



    @Test
    @DisplayName("로그인 성공")
    void passLogin() throws Exception{
        // Given
        Member member = new Member("valid@example.com","validPassword");
        memberRepository.save(member);

        // When & Then
        mockMvc.perform(post("/members/login")
                .contentType("application/json")
                .content("{ \"email\": \"valid@example.com\", \"password\": \"validPassword\" }"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("로그인 실패 - 이메일 또는 패스워드 틀림")
    void failLogin() throws Exception{
        // Given
        Member member = new Member("invalid@example.com","invalidPassword");

        // When & Then
        mockMvc.perform(post("/members/login")
                .contentType("application/json")
                .content("{ \"email\": \"invalid@example.com\", \"password\": \"invalidPassword\" }"))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message").value("사용자 없거나 비밀번호 틀림"));
    }

}
