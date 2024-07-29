package gift.user.service;

import gift.core.domain.user.User;
import gift.core.domain.user.UserAccountRepository;
import gift.core.domain.user.UserRepository;
import gift.core.domain.user.UserService;
import gift.core.domain.user.exception.UserAlreadyExistsException;
import gift.core.domain.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserAccountRepository userAccountRepository) {
        this.userRepository = userRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public void registerUser(User user) {
        if (userRepository.existsById(user.id())) {
            throw new UserAlreadyExistsException();
        }
        if (userAccountRepository.existsByPrincipal(user.account().principal())) {
            throw new UserAlreadyExistsException();
        }
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
