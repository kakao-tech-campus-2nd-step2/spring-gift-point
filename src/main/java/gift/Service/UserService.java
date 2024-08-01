package gift.Service;

import gift.DTO.AuthRequestDTO;
import gift.DTO.AuthResponseDTO;
import gift.DTO.UserDTO;
import gift.Entity.UserEntity;
import gift.Mapper.UserServiceMapper;
import gift.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceMapper userServiceMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserServiceMapper userServiceMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userServiceMapper = userServiceMapper;
    }

    // 회원 정보 조회
    public Optional<UserDTO> findUserById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        return userEntity.map(userServiceMapper::convertToDTO);
    }

    // 회원 저장
    public UserDTO saveUser(UserDTO userDTO) {
        UserEntity userEntity = userServiceMapper.convertToEntity(userDTO);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return userServiceMapper.convertToDTO(savedUserEntity);
    }

    // 회원 정보 업데이트
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        Optional<UserEntity> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            UserEntity user = existingUser.get();
            user.setEmail(userDTO.getEmail());
            UserEntity updatedUserEntity = userRepository.save(user);
            return userServiceMapper.convertToDTO(updatedUserEntity);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // 회원 삭제
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }




    // 로그인
    public AuthResponseDTO loginUser(AuthRequestDTO authRequest) {
        UserEntity userEntity = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("이메일이 잘못 입력되었습니다."));

        if (passwordEncoder.matches(authRequest.getPassword(), userEntity.getPassword())) {
            String accessToken = generateToken(userEntity); // 엑세스 토큰
            String refreshToken = generateRefreshToken(userEntity);
            return new AuthResponseDTO(accessToken, refreshToken);
        } else {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }
    }



    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;


    // 액세스 토큰 생성
    public String generateToken(UserEntity userEntity) {
        Claims claims = Jwts.claims().setSubject(userEntity.getEmail());
        claims.put("userId", userEntity.getId());
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(UserEntity userEntity) {
        Claims claims = Jwts.claims().setSubject(userEntity.getEmail());
        claims.put("userId", userEntity.getId());
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + refreshExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // 액세스 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject() != null && !claims.getExpiration().before(new Date()); // 유효기간 확인
        } catch (Exception e) {
            return false;
        }
    }

    // 리프레시 토큰 유효성 검사
    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject() != null && !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // 액세스 토큰 갱신
    public String refreshAccessToken(String refreshToken) {
        if (validateRefreshToken(refreshToken)) {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(refreshToken)
                    .getBody();
            Long userId = claims.get("userId", Long.class);
            UserEntity userEntity = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            return generateToken(userEntity);
        } else {
            throw new RuntimeException("유효하지 않은 리프레쉬 토큰");
        }
    }

    // 토큰에서 사용자 정보 추출
    public Optional<UserEntity> getUserFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            Long userId = claims.get("userId", Long.class);
            return userRepository.findById(userId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
