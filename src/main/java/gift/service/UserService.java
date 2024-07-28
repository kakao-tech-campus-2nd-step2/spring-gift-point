package gift.service;

import gift.domain.User;
import gift.dto.requestdto.UserLoginRequestDTO;
import gift.dto.requestdto.UserSignupRequestDTO;
import gift.repository.JpaUserRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final JpaUserRepository jpaUserRepository;

    public UserService(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    public User join(UserSignupRequestDTO userSignupRequestDTO) {
        User user = userSignupRequestDTO.toEntity();
        return jpaUserRepository.save(user);
    }

    public User findById(Long id) {
        return jpaUserRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("회원의 정보가 일치하지 않습니다."));
    }

    public Optional<User> findByEmail(String email){
        return jpaUserRepository.findByEmail(email);
    }

    public User findByEmail(UserLoginRequestDTO userLoginRequestDTO) {
        return jpaUserRepository.findByEmail(userLoginRequestDTO.email())
            .orElseThrow(() -> new NoSuchElementException("회원의 정보가 일치하지 않습니다."));
    }
}
