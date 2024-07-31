package gift.service;

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
}
