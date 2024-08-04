package gift.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final KakaoUserRepository kakaoUserRepository;

    public UserService(UserRepository userRepository, KakaoUserRepository kakaoUserRepository) {
        this.userRepository = userRepository;
        this.kakaoUserRepository = kakaoUserRepository;
    }

    public User registerUser(UserDTO userDTO) {

        return userRepository.save(
                new User(
                        userDTO.getEmail(),
                        userDTO.getPassword(),
                        UserType.NORMAL_USER));
    }

    public KakaoUser registerKakaoUser(KakaoUserDTO kakaoUserDTO) {
        User user = new User(null, null, UserType.KAKAO_USER);
        userRepository.save(user);
        KakaoUser kakaoUser = new KakaoUser(
                kakaoUserDTO.getId(),
                kakaoUserDTO.getAccessToken(),
                kakaoUserDTO.getRefreshToken()
        );
        kakaoUser.setUser(user);
        return kakaoUserRepository.save(kakaoUser);
    }

    public Optional<KakaoUser> findByKakaoSocialID(Long id){
        return kakaoUserRepository.findById(id);
    }

    @Transactional
    public KakaoUser updateKakaoUserToken(KakaoUserDTO kakaoUserDTO){
        KakaoUser kakaoUser = findByKakaoSocialID(kakaoUserDTO.getId()).orElseThrow();
        kakaoUser.setAccessToken(kakaoUser.accessToken);
        kakaoUser.setRefreshToken(kakaoUser.refreshToken);
        return kakaoUser;
    }

    public boolean validateUser(LoginDTO loginDTO) {
        User user = getUserByEmail(loginDTO.email);
        return Objects.equals(loginDTO.getPassword(), user.getPassword());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    public KakaoUser getKakaoUserByUser(User user){
        return kakaoUserRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Kakao User not found with User: " + user.getId()));
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

}
