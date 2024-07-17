package gift.user.service;

import gift.user.exception.ForbiddenException;
import gift.user.model.UserRepository;
import gift.user.model.dto.AppUser;
import gift.user.model.dto.SignUpRequest;
import gift.user.model.dto.UpdatePasswordRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.SHA256Util;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        String salt = SHA256Util.getSalt();
        String hashedPassword = SHA256Util.encodePassword(signUpRequest.getPassword(), salt);
        signUpRequest.setPassword(hashedPassword);
        AppUser appUser = new AppUser(signUpRequest.getEmail(), signUpRequest.getPassword(), signUpRequest.getRole(),
                salt);
        userRepository.save(appUser);
    }

    @Transactional
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest, AppUser loginAppUser) {
        if (!loginAppUser.isPasswordCorrect(updatePasswordRequest.oldPassword())) {
            throw new ForbiddenException("비밀번호 변경 실패: 기존 비밀번호 불일치");
        }

        String newHashedPassword = SHA256Util.encodePassword(updatePasswordRequest.newPassword(),
                loginAppUser.getSalt());

        loginAppUser.setPassword(newHashedPassword);
        userRepository.save(loginAppUser);
    }

    @Transactional(readOnly = true)
    public AppUser findUser(Long id) {
        return userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("AppUser"));
    }

    @Transactional(readOnly = true)
    public String findEmail(Long id) {
        AppUser appUser = userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("AppUser"));
        return appUser.getEmail();
    }

    public void verifyAdminAccess(AppUser appUser) {
        if (!appUser.isAdmin()) {
            throw new ForbiddenException("해당 요청에 대한 관리자 권한이 없습니다.");
        }
    }
}