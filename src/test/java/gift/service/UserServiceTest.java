package gift.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.controller.user.dto.UserRequest.Create;
import gift.model.User;
import gift.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;

@ExtendWith(MockitoExtension.class)
@Sql("/truncate.sql")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입")
    void register() {
        given(userRepository.save(any())).willReturn(
            new User(null, "yso8296", "yso8296@gmail.com"));
        given(userRepository.existsByEmail(any())).willReturn(false);

        userService.register(new Create("yso8296", "yso8296@gmail.com"));

        then(userRepository).should().save(any());
    }
}
