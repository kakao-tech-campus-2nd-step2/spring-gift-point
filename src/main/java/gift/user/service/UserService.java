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
import gift.dto.request.OptionRequest;
import gift.dto.request.OrderRequest;
import gift.dto.request.SignupRequest;
import gift.product.repository.OptionJpaRepository;
import gift.product.repository.ProductJpaRepository;
import gift.user.domain.Role;
import gift.user.domain.User;
import gift.user.repository.UserJpaRepository;

@Service
public class UserService {
	private final UserJpaRepository userJpaRepository;
	private final OptionJpaRepository optionJpaRepository;
	private final JwtProvider jwtProvider;
	private final KakaoRestClient kakaoRestClient;
	private final CacheManager cacheManager;

	public UserService(UserJpaRepository userJpaRepository, OptionJpaRepository optionJpaRepository,
		JwtProvider jwtProvider, KakaoRestClient kakaoRestClient, CacheManager cacheManager) {
		this.userJpaRepository = userJpaRepository;
		this.optionJpaRepository = optionJpaRepository;
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
	public void sendKakaoMessage(OrderRequest orderRequest, Long userId) {
		String email = userJpaRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId)).getEmail();
		String accessToken = cacheManager.getCache("userToken").get(email, String.class);

		kakaoRestClient.sendKakaoMessage("application/x-www-form-urlencoded;charset=utf-8", "Bearer " + accessToken,
			TemplateObject.of(orderRequest.message()).toUrlEncoded());
	}

	@Transactional
	public void usePoints(OrderRequest orderRequest, Long userId) {
		User user = userJpaRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		Long price = optionJpaRepository.findById(orderRequest.optionId()).orElseThrow().getProduct().getPrice();
		user.usePoint(price);
		userJpaRepository.save(user);
	}

	@Transactional
	public Long getPoints(Long userId) {
		User user = userJpaRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		return user.getPoint();
	}

	@Transactional
	public Long addPoints(Long userId, Long points) {
		User user = userJpaRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		user.addPoint(points);
		userJpaRepository.save(user);
		return user.getPoint();
	}
}
