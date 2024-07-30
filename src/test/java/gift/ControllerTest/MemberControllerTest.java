package gift.ControllerTest;


import gift.model.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        memberRepository.deleteAll();
    }

    @Test
    void testLogout() throws Exception {
        Member member = new Member("test@example.com", "password");
        memberRepository.save(member);

        String token = JwtUtility.generateToken(member.getEmail());
        member = new Member(member, token);
        memberRepository.save(member);

        mockMvc.perform(post("/api/members/logout")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\": \"로그아웃 되었습니다.\"}"));
    }
}
