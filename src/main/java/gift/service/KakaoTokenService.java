package gift.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gift.dto.response.KakaoTokenResponse;
import gift.dto.response.RefreshTokenResponse;
import gift.entity.KakaoToken;
import gift.exception.CustomException;
import gift.repository.KakaoTokenRepository;
import gift.util.JwtUtil;
import jakarta.transaction.Transactional;

@Service
public class KakaoTokenService {
    
    private KakaoTokenRepository kakaoTokenRepository;
    private JwtUtil jwtUtil;
    private KakaoApiService kakaoApiService;

    public KakaoTokenService(KakaoTokenRepository kakaoTokenRepository, JwtUtil jwtUtil){
        this.kakaoTokenRepository = kakaoTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void saveKakaoToken(String email, KakaoTokenResponse kakaoTokenResponse){

        if(kakaoTokenRepository.findByEmail(email).isEmpty()){
            KakaoToken kakaoToken = new KakaoToken(email, kakaoTokenResponse.getAccessToken(), kakaoTokenResponse.getRefreshToken(), kakaoTokenResponse.getExpiresIn());
            kakaoTokenRepository.save(kakaoToken);
        }
    }

    public String findKakaoToken(String token){

        String email = (String)jwtUtil.extractAllClaims(token).get("email");
        
        KakaoToken kakaoToken = kakaoTokenRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException("KakaoToken is not exists", HttpStatus.NOT_FOUND));
        
        return kakaoToken.getAccessToken();
    }

    public void refreshToken(String email, KakaoToken kakaoToken){
        
        if(kakaoToken.isExpired()){

            RefreshTokenResponse refreshTokenResponse = kakaoApiService.refreshToken(kakaoToken.getRefreshToken());

            KakaoToken saveToken = new KakaoToken(email, 
            refreshTokenResponse.getAccessToken(), 
            refreshTokenResponse.getRefreshToken(),
            refreshTokenResponse.getExpiresIn());

            kakaoTokenRepository.delete(kakaoToken);
            kakaoTokenRepository.save(saveToken);
        }
    }
}
