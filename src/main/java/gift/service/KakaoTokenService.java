package gift.service;

import gift.common.exception.AuthenticationException;
import gift.model.KakaoAccessToken;
import gift.model.KakaoRefreshToken;
import gift.repository.AccessTokenRepository;
import gift.repository.RefreshTokenRepository;
import gift.service.dto.KakaoTokenDto;
import org.springframework.stereotype.Service;

@Service
public class KakaoTokenService {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final KakaoApiCaller kakaoApiCaller;

    public KakaoTokenService(AccessTokenRepository accessTokenRepository, RefreshTokenRepository refreshTokenRepository, KakaoApiCaller kakaoApiCaller) {
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.kakaoApiCaller = kakaoApiCaller;
    }

    public void saveToken(Long memberId, KakaoTokenDto kakaoTokenDto) {
        accessTokenRepository.save(new KakaoAccessToken(memberId, kakaoTokenDto.accessToken(), kakaoTokenDto.expiresIn()));
        refreshTokenRepository.save(new KakaoRefreshToken(memberId, kakaoTokenDto.refreshToken(), kakaoTokenDto.refreshTokenExpiresIn()));
    }

    public void deleteAccessToken(Long memberId) {
        accessTokenRepository.deleteById(memberId);
    }

    public void deleteRefreshToken(Long memberId) {
        refreshTokenRepository.deleteById(memberId);
    }

    public String refreshIfAccessTokenExpired(Long memberId) {
            return accessTokenRepository.findById(memberId)
                    .orElseGet(() -> {
                        String refreshToken = refreshTokenRepository.findById(memberId)
                                .orElseThrow(() -> new AuthenticationException("Login has expired"))
                                .getRefreshToken();
                        KakaoTokenDto tokenDto = kakaoApiCaller.refreshAccessToken(refreshToken);
                        return new KakaoAccessToken(memberId, tokenDto.accessToken(), tokenDto.expiresIn());
                    }).getAccessToken();
        }

}
