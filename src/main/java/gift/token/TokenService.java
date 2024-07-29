package gift.token;

import gift.users.kakao.KakaoTokenDTO;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void saveToken(Long userId, KakaoTokenDTO kakaoTokenDTO, String sns){
        if(tokenRepository.existsByUserIdAndSns(userId, sns)){
            return;
        }
        Token token = new Token(userId, sns, kakaoTokenDTO.accessToken(),
            kakaoTokenDTO.expiresIn(), kakaoTokenDTO.refreshToken(), kakaoTokenDTO.refreshTokenExpiresIn());
        tokenRepository.save(token);
    }

    public String findToken(long userId, String sns){
        return tokenRepository.findByUserIdAndSns(userId, sns).getAccessToken();
    }
}
