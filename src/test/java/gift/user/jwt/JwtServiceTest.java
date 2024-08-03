package gift.user.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gift.domain.AppUser;
import gift.domain.Role;
import gift.repository.UserRepository;
import gift.util.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    @Test
    public void testCreateToken() {
        Long id = 1L;
        String token = jwtService.createToken(id);
        assertNotNull(token);
        System.out.println("Generated Token: " + token);
    }

    @Test
    public void testGetEmailFromToken() {
        Long id = 1L;
        AppUser appUser = new AppUser("yoo@example.com", "1234", Role.ADMIN, "1");
        appUser.setId(id);
        userRepository.save(appUser);
        String token = jwtService.createToken(id);

        AppUser loginAppUser = jwtService.getLoginUser(token);
        assertEquals(appUser.getId(), loginAppUser.getId());
    }
}
