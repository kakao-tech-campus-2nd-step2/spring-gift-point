package gift;

import gift.controller.MemberController;
import gift.model.Member;
import gift.service.MemberService;
import gift.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import jakarta.servlet.ServletException;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MemberService memberService;

  @MockBean
  private JwtUtil jwtUtil;

  private Member member;

  @BeforeEach
  public void setUp() {
    member = new Member(null, "test@example.com", "password", "Test User");
  }

  @Test
  public void testRegister() throws Exception {
    when(memberService.register(any(Member.class))).thenReturn("test-token");

    mockMvc.perform(post("/members/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\": \"test@example.com\", \"password\": \"password\"}"))
            .andExpect(status().isOk())
            .andExpect(content().string("test-token"));
  }

  @Test
  public void testRegisterWithError() throws Exception {
    when(memberService.register(any(Member.class))).thenThrow(new RuntimeException("Error during registration"));

    Exception exception = assertThrows(ServletException.class, () -> {
      mockMvc.perform(post("/members/register")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{\"email\": \"test@example.com\", \"password\": \"password\"}"))
              .andExpect(status().isInternalServerError());
    });

    assertThat(exception.getCause().getMessage(), containsString("Error during registration"));
  }

  @Test
  public void testLogin() throws Exception {
    when(memberService.login("test@example.com", "password")).thenReturn("test-token");

    mockMvc.perform(post("/members/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\": \"test@example.com\", \"password\": \"password\"}"))
            .andExpect(status().isOk())
            .andExpect(content().string("test-token"));
  }

  @Test
  public void testLoginWithInvalidCredentials() throws Exception {
    when(memberService.login("test@example.com", "password")).thenThrow(new IllegalArgumentException("Invalid email or password"));

    mockMvc.perform(post("/members/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\": \"test@example.com\", \"password\": \"password\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Invalid email or password"));
  }
}
