package gift.filter;

import gift.entity.User;
import gift.util.UserUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class AdminFilterTest {

    private AdminFilter adminFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @Autowired
    private UserUtility userUtility;
    @Value("${spring.var.token-prefix}")
    private String tokenPrefix;

    @BeforeEach
    public void setUp() {
        adminFilter = new AdminFilter(tokenPrefix, userUtility);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @Test
    void 토큰_없이_필터_테스트() throws ServletException, IOException {
        // given
        given(request.getHeader("Authorization")).willReturn(null);

        // when
        adminFilter.doFilter(request, response, filterChain);

        // then
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @Test
    void admin_토큰_필터_테스트() throws ServletException, IOException {
        // given
        User admin = new User("admin@naver.com", "admin");
        admin.setRole("ADMIN");
        String adminToken = userUtility.makeAccessToken(admin);
        given(request.getHeader("Authorization")).willReturn(tokenPrefix + adminToken);

        // when
        adminFilter.doFilter(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void user_토큰_필터_테스트() throws ServletException, IOException {
        // given
        String userToken = userUtility.makeAccessToken(new User("test@naver.com", "test"));
        given(request.getHeader("Authorization")).willReturn(tokenPrefix + userToken);

        // when
        adminFilter.doFilter(request, response, filterChain);

        // then
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
