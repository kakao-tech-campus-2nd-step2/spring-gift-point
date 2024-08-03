package gift.service;

import gift.dto.point.PointResponseDTO;
import gift.dto.user.UserDTO;
import gift.exceptions.CustomException;
import gift.model.User;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void createUser(UserDTO userDTO) {
        userRepository.save(new User(userDTO.email(),
                userDTO.password(),
                "user",
                0L));
    }

    public User findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(CustomException::userNotFoundException);
    }

    public PointResponseDTO findPoint(Long userId) {
        Long point = userRepository.getPointById(userId);
        return new PointResponseDTO(point);
    }

    public void kakaoSign(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            String tempPassword = UUID.randomUUID().toString();
            userRepository.save(new User(email, tempPassword, "kakao", 0L));
        }
    }
}
