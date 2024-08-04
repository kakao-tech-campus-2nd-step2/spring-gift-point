package gift.user;



import gift.model.SiteUser;
import gift.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private static final int INITIAL_POINTS = 1000;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}


	public SiteUser create(String username, String email, String password) {
		String encodedPassword = passwordEncoder.encode(password);
		SiteUser user = new SiteUser(username, email, encodedPassword, INITIAL_POINTS);
		this.userRepository.save(user);
		return user;
	}
}
