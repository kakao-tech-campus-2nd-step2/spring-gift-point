package gift.repository;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import gift.controller.dto.ChangePasswordDTO;
import gift.domain.UserInfo;
import gift.utils.error.UserNotFoundException;
import gift.utils.error.UserPasswordNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserInfoRepositoryTest {

    @Autowired
    private UserInfoRepository userInfoRepository;


    @Test
    @DisplayName("이메일로 찾기 테스트")
    void findByEmail() {
        UserInfo userInfo = new UserInfo("kakaocampus@gmail.com", "kakao2024");
        UserInfo save = userInfoRepository.save(userInfo);
        Optional<UserInfo> byEmail = userInfoRepository.findByEmail(userInfo.getEmail());

        assertThat(byEmail).isPresent();
        assertThat(save.getEmail()).isEqualTo(byEmail.get().getEmail());
        assertThat(save.getPassword()).isEqualTo(byEmail.get().getPassword());
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 찾기 테스트")
    void findByNonExistentEmail() {
        Optional<UserInfo> byEmail = userInfoRepository.findByEmail("nonexistent@example.com");
        assertThat(byEmail).isEmpty();
    }

    @Test
    @DisplayName("사용자 생성 테스트")
    void CreateUserInfo() {
        //Given
        UserInfo userInfo = new UserInfo("kakako@gmail.com", "kakao2024");
        //When
        userInfoRepository.save(userInfo);
        //Then
        Optional<UserInfo> byEmail = userInfoRepository.findByEmail(userInfo.getEmail());
        assertThat(byEmail).isPresent();
        assertThat(byEmail.get().getEmail()).isEqualTo(userInfo.getEmail());
        assertThat(byEmail.get().getPassword()).isEqualTo(userInfo.getPassword());

    }


}
