package gift.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gift.entity.User;
import gift.exception.UserNotFoundException;
import gift.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final TokenService tokenService; 
	
	public UserService(UserRepository userRepository, TokenService tokenService) {
		this.userRepository = userRepository;
		this.tokenService = tokenService;
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
	    		.orElseThrow(() -> new UserNotFoundException("email not found"));
	}
	
	public User getUserFromToken(String token) {
    	String email = tokenService.extractionEmail(token);
    	return findByEmail(email);
    }
	
	 public User findById(Long userId) {
	        return userRepository.findById(userId)
	                .orElseThrow(() -> new UserNotFoundException("user not found."));
	    }
	
	public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
