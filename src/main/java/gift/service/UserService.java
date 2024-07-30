package gift.service;

import gift.DTO.User.UserRequest;
import gift.DTO.User.UserResponse;
import gift.domain.User;
import gift.repository.UserRepository;
import gift.security.KakaoTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * 유저 정보를 오름차순으로 조회하는 로직
     */
    public Page<UserResponse> findAllASC(int page, int size, String field) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc(field));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));

        Page<User> users = userRepository.findAll(pageable);

        return users.map(UserResponse::new);
    }

    /*
     * 유저 정보를 내림차순으로 조회하는 로직
     */
    public Page<UserResponse> findAllDESC(int page, int size, String field) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc(field));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));

        Page<User> users = userRepository.findAll(pageable);

        return users.map(UserResponse::new);
    }

    /*
     * email을 기준으로 한 유저를 조회하는 로직
     */
    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return new UserResponse(user);
    }

    /*
     * id를 기준으로 한 유저를 조회하는 로직
     */
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(NoSuchFieldError::new);
        return new UserResponse(user);
    }

    /*
     * 유저 정보를 저장하는 로직
     */
    @Transactional
    public UserResponse save(UserRequest userRequest) {
        User savedUser = userRepository.save(new User(
                userRequest.getEmail(),
                userRequest.getPassword()
        ));

        return new UserResponse(savedUser);
    }

    /*
     * 카카오 로그인 유저 정보를 저장하는 로직
     */
    @Transactional
    public UserResponse saveKakao(String kakaoId, String token) {
        if (userRepository.existsByEmail(kakaoId + "@email.com")) {
            return new UserResponse(userRepository.findByEmail(kakaoId + "@email.com"));
        }

        User savedKakaoUser = userRepository.save(new User(
                kakaoId + "@email.com",
                UUID.randomUUID().toString()
        ));
        savedKakaoUser.insertToken(token);

        return new UserResponse(savedKakaoUser);
    }

    /*
     * 유저 정보를 갱신하는 로직
     */
    @Transactional
    public void update(UserRequest userRequest) {
        User savedUser = userRepository.findByEmail(userRequest.getEmail());
        savedUser.updateEntity(userRequest.getEmail(), userRequest.getPassword());
    }

    /*
     * Email의 중복 여부를 확인하는 로직
     */
    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    /*
     * 로그인을 위한 확인을 해주는 로직
     */
    public boolean login(UserRequest userRequest) {
        return userRepository.existsByEmailAndPassword(userRequest.getEmail(), userRequest.getPassword());
    }

    /*
     * 유저 정보를 삭제하는 로직
     */
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
