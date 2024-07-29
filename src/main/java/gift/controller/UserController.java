package gift.controller;

import gift.dto.UserDTO;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for user management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    @Operation(summary = "Show registration form", description = "This API returns the registration form.")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "Register";
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "This API registers a new user.")
    public String register(@Valid @ModelAttribute UserDTO userDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDTO);
            return "Register";
        }
        userService.register(userDTO);
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    @Operation(summary = "Show login form", description = "This API returns the login form.")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "Login";
    }

    @PostMapping("/login")
    @ResponseBody
    @Operation(summary = "User login", description = "This API handles user login and returns a token if successful.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully logged in."),
        @ApiResponse(responseCode = "401", description = "Invalid email or password."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<Map<String, Object>> login(@ModelAttribute UserDTO userDTO) {
        String token = userService.login(userDTO.getEmail(), userDTO.getPassword());
        Map<String, Object> response = new HashMap<>();
        if (token != null) {
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}