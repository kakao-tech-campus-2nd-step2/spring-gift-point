package gift.users.user;

import gift.error.NotFoundIdException;
import gift.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public Long findBySnsIdAndSnsAndRegisterIfNotExists(String snsId, String sns) {
        if (!userRepository.existsBySnsIdAndSns(snsId, sns)) {
            userRepository.save(new User(snsId, sns));
        }
        User user = userRepository.findBySnsIdAndSns(snsId, sns);
        return user.getId();
    }

    public String findSns(Long userId){
        User user = findUserById(userId);
        return user.getSns();
    }

    public String loginGiveJwt(String userId) {
        String jwtToken = jwtUtil.generateToken(userId);
        if (jwtToken != null) {
            return "access-token: " + jwtToken;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰 생성 실패");
    }

    public UserDTO register(UserDTO user) {
        String password = user.password();
        String email = user.email();
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일과 비밀번호는 빈칸일 수 없습니다.");
        }
        return registerUser(user);
    }

    public String login(UserDTO user) {
        String email = user.email();
        String password = user.password();
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일과 비밀번호는 빈칸일 수 없습니다.");
        }
        if (!getUserByEmailAndPassword(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        }
        UserDTO userDTO = findUserByEmail(email);
        return loginGiveJwt(userDTO.id().toString());
    }

    public UserDTO registerUser(UserDTO userDTO) {
        User user = userDTO.toUser();
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하는 이메일입니다.");
        }
        return UserDTO.fromUser(userRepository.save(user));
    }

    private User findUserById(Long id){
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundIdException("없는 회원 아이디입니다."));
    }

    public UserDTO findById(long id) {
        return UserDTO.fromUser(findUserById(id));
    }

    public UserDTO findUserByEmail(String email) {
        return UserDTO.fromUser(userRepository.findByEmail(email));
    }

    public boolean getUserByEmailAndPassword(UserDTO userDTO) {
        User user = userDTO.toUser();
        return userRepository.existsByEmailAndPassword(user.getEmail(), user.getPassword());
    }

    public void updatePoints(User user){
        userRepository.save(user);
    }
}
