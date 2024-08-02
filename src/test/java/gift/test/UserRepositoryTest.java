package gift.test;

import gift.model.SiteUser;
import gift.repository.UserRepository;
import gift.user.UserCreateForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebApplicationContext context;
    private PasswordEncoder passwordEncoder;
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Test
    @DisplayName("회원가입할때 정상적으로 작동되는 경우")
    void testUserRegistration() {
        userRepository.deleteAll();
        // Given
        SiteUser user = new SiteUser();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("test@example.com");

        // When
        SiteUser savedUser = userRepository.save(user);

        // Then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }


    @Test
    @DisplayName("잘못된 아이디 또는 비밀번호로 로그인 실패할때 예외발생")
    void testLoginWithInvalidCredentials() throws Exception {
        // Given
        UserCreateForm form = new UserCreateForm();
        form.setUsername("invalidUser");
        form.setPassword("wrongPassword");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("{ \"username\": \"invalidUser\", \"password\": \"wrongPassword\" }"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.error").value("Invalid login credentials"));
    }

    @Test
    @DisplayName("로그인 성공할 경우 정상 작동")
    void testLoginWithValidCredentials() throws Exception {
        // Given
        SiteUser user = new SiteUser();
        user.setUsername("validUser");
        user.setPassword(passwordEncoder.encode("validPassword"));
        user.setEmail("valid@example.com");
        userRepository.save(user);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("{ \"username\": \"validUser\", \"password\": \"validPassword\" }"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists());
    }
}
