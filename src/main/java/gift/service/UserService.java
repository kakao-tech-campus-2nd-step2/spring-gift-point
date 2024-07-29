package gift.service;

import gift.controller.dto.ChangePasswordDTO;
import gift.controller.dto.TokenResponseDTO;
import gift.controller.dto.UserDTO;
import gift.domain.UserInfo;
import gift.repository.UserInfoRepository;
import gift.utils.JwtTokenProvider;
import gift.utils.error.UserAlreadyExistsException;
import gift.utils.error.UserNotFoundException;
import gift.utils.error.UserPasswordNotFoundException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserInfoRepository userInfoRepository;
    private final JwtTokenProvider jwtTokenProvider;


    public UserService(UserInfoRepository userInfoRepository, JwtTokenProvider jwtTokenProvider) {
        this.userInfoRepository = userInfoRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Transactional
    public TokenResponseDTO registerUser(UserDTO userDTO) {
        UserInfo userInfo = new UserInfo(userDTO.getEmail(), userDTO.getPassword());
        if (userInfo.getId() != null && userInfoRepository.existsById(userInfo.getId())) {
            throw new UserAlreadyExistsException("User Already Exist");
        }
        userInfoRepository.save(userInfo);
        return new TokenResponseDTO(jwtTokenProvider.createToken(userDTO.getEmail()));
    }

    public TokenResponseDTO login(UserDTO userDTO) {
        UserInfo userInfo = new UserInfo(userDTO.getEmail(), userDTO.getPassword());
        Optional<UserInfo> byEmail = userInfoRepository.findByEmail(userInfo.getEmail());
        if (byEmail.isEmpty()) {
            throw new UserNotFoundException("User NOT FOUND");
        }
        return new TokenResponseDTO(jwtTokenProvider.createToken(userDTO.getEmail()));
    }
    @Transactional
    public boolean changePassword(ChangePasswordDTO changePasswordDTO) {
        UserInfo userInfo = userInfoRepository.findByEmail(changePasswordDTO.getEmail())
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!userInfo.getPassword().equals(changePasswordDTO.getCurrentPassword())) {
            throw new UserPasswordNotFoundException("Password not found");
        }
        userInfo.setPassword(changePasswordDTO.getNewPassword());
        userInfoRepository.save(userInfo);
        return true;
    }

    public UserDTO findPassword(String email) {
        System.out.println(email);
        UserInfo byEmail = userInfoRepository.findByEmail(email).orElseThrow(
            () -> new UserNotFoundException("User NOT FOUND"));
        return new UserDTO(byEmail.getPassword(), byEmail.getEmail());
    }


}
