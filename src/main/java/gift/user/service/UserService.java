package gift.user.service;

import java.util.Set;
import java.util.UUID;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.core.api.kakao.KakaoRestClient;
import gift.core.api.kakao.dto.TemplateObject;
import gift.core.exception.user.PasswordNotMatchException;
import gift.core.exception.user.UserAlreadyExistsException;
import gift.core.exception.user.UserNotFoundException;
import gift.core.jwt.JwtProvider;
import gift.dto.request.SignupRequest;
import gift.user.domain.Role;
import gift.user.domain.User;
import gift.user.repository.UserJpaRepository;

@Service
public class UserService {
	private final UserJpaRepository userJpaRepository;
	private final JwtProvider jwtProvider;
	private final KakaoRestClient kakaoRestClient;
	private final CacheManager cacheManager;

	public UserService(UserJpaRepository userJpaRepository, JwtProvider jwtProvider, KakaoRestClient kakaoRestClient,
		CacheManager cacheManager) {
		this.userJpaRepository = userJpaRepository;
		this.jwtProvider = jwtProvider;
		this.kakaoRestClient = kakaoRestClient;
		this.cacheManager = cacheManager;
	}

	@Transactional
	public void registerUser(SignupRequest userDto) {
		userJpaRepository.findByEmail(userDto.email()).ifPresent((eUser) -> {
			throw new UserAlreadyExistsException(eUser.getEmail());
		});
		userJpaRepository.save(userDto.toEntity());
	}

	@Transactional(readOnly = true)
	public String loginUser(String email, String password) {
		User user = userJpaRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

		if (!user.getPassword().equals(password)) {
			throw new PasswordNotMatchException();
		}
		return jwtProvider.generateToken(user.getId().toString(), user.getRole());
	}

	@Transactional
	public String loginOauth2User(String email) {
		User user = userJpaRepository.findByEmail(email).orElseGet(() -> {
			User newUser = User.of(email, UUID.randomUUID().toString(), Set.of(Role.USER));
			return userJpaRepository.save(newUser);
		});
		return jwtProvider.generateToken(user.getId().toString(), user.getRole());
	}

	@Transactional
	public void sendKakaoMessage(String message, Long userId) {
		String email = userJpaRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId)).getEmail();
		String accessToken = cacheManager.getCache("userToken").get(email, String.class);

		kakaoRestClient.sendKakaoMessage("application/x-www-form-urlencoded;charset=utf-8", "Bearer " + accessToken,
			TemplateObject.of(message).toUrlEncoded());
	}
}
