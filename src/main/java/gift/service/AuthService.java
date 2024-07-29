package gift.service;

import gift.dto.UserDTO;
import gift.exceptions.CustomException;
import gift.model.User;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void redundantUser(String state, UserDTO userDTO) {
        boolean userExists = userRepository.existsByEmail(userDTO.email());

        if (state.equals("login") & !userExists) {
            throw CustomException.userNotFoundException();
        }

        if (state.equals("regist") & userExists) {
            throw CustomException.redundantEmailException();
        }
    }

    public void comparePassword(UserDTO userDTO) {
        User realUser = userRepository.findByEmail(userDTO.email()).orElse(null);

        if(!realUser.getPassword().equals(userDTO.password())) {
            throw CustomException.invalidPasswordException();
        }
    }
}
