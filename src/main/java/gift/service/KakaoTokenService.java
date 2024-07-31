package gift.service;

import gift.domain.KakaoToken;
import gift.repository.KakaoTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KakaoTokenService {

    private final KakaoTokenRepository kakaoTokenRepository;

    public KakaoTokenService(KakaoTokenRepository kakaoTokenRepository) {
        this.kakaoTokenRepository = kakaoTokenRepository;
    }

    public void saveKakaoToken(String userEmail, String token) {
        KakaoToken kakaoToken = kakaoTokenRepository.findByUserEmail(userEmail)
                .orElse(new KakaoToken(userEmail, token));
        kakaoToken.withUpdatedToken(token);
        kakaoTokenRepository.save(kakaoToken);
    }

    public String getTokenByEmail(String email) {
        return kakaoTokenRepository.findByUserEmail(email)
                .map(KakaoToken::getToken)
                .orElse(null);
    }
}