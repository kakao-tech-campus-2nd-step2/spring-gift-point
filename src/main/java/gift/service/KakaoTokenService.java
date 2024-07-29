package gift.service;

import static gift.controller.auth.AuthMapper.toKakaoTokenResponse;

import gift.controller.auth.KakaoTokenResponse;
import gift.domain.KakaoToken;
import gift.exception.KakaoTokenInvalidException;
import gift.repository.KakaoTokenRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KakaoTokenService {

    private final KakaoTokenRepository kakaoTokenRepository;

    public KakaoTokenService(KakaoTokenRepository kakaoTokenRepository) {
        this.kakaoTokenRepository = kakaoTokenRepository;
    }

    @Transactional(readOnly = true)
    public KakaoTokenResponse findAccessTokenByMemberId(UUID memberId) {
        return toKakaoTokenResponse(kakaoTokenRepository.findByMemberId(memberId)
            .orElseThrow(KakaoTokenInvalidException::new));
    }

    @Transactional
    public void save(UUID memberId, KakaoTokenResponse kakaoToken) {
        kakaoTokenRepository.save(new KakaoToken(memberId, kakaoToken.getAccessToken()));
    }
}
