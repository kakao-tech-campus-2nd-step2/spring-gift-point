package gift.controller;

import gift.controller.dto.ChangePasswordDTO;
import gift.controller.dto.TokenResponseDTO;
import gift.controller.dto.UserDTO;
import gift.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/members")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        TokenResponseDTO tokenResponseDTO = userService.registerUser(userDTO);
        return new ResponseEntity<>(tokenResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> loginUser(@Valid @RequestBody UserDTO userDTO) {
        TokenResponseDTO login = userService.login(userDTO);
        return ResponseEntity.ok().body(login);
    }

    @PutMapping("/change")
    public ResponseEntity<Boolean> changePassword(
        @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        boolean b = userService.changePassword(changePasswordDTO);
        return ResponseEntity.ok(b);
    }

    @PostMapping("/find")
    public ResponseEntity<UserDTO> findPassword(@RequestParam("email") String email) {
        UserDTO password = userService.findPassword(email);
        return ResponseEntity.ok(password);
    }

}

