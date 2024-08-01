package gift.service;

import gift.dto.TokenDTO;
import gift.dto.user.UserDTO;
import gift.exceptions.CustomException;
import gift.model.User;
import gift.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Void> createResponse(TokenDTO tokenDTO) {
        var headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokenDTO.token());

        return new ResponseEntity<>(headers, HttpStatus.OK);
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
