//package gift.ControllerTest;
//
//
//import gift.model.Member;
//import gift.repository.MemberRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class MemberControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @BeforeEach
//    void setup() {
//        memberRepository.deleteAll();
//    }
//
//    @Test
//    void testRegister() throws Exception {
//        mockMvc.perform(post("/api/members/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{ \"email\": \"test@example.com\", \"password\": \"password\" }"))
//                .andExpect(status().isOk())
//                .andExpect(header().exists(HttpHeaders.AUTHORIZATION));
//    }
//
//    @Test
//    void testLogin() throws Exception {
//        Member member = new Member("test@example.com", "password");
//        memberRepository.save(member);
//
//        mockMvc.perform(post("/api/members/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{ \"email\": \"test@example.com\", \"password\": \"password\" }"))
//                .andExpect(status().isOk())
//                .andExpect(header().exists(HttpHeaders.AUTHORIZATION));
//    }
//}
