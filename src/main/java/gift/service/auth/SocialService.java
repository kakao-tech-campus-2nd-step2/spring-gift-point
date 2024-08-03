package gift.service.auth;

import gift.domain.auth.KakaoToken.kakaoInfo;
import gift.domain.auth.KakaoToken.kakaoToken;
import gift.entity.auth.SocialEntity;
import gift.entity.auth.UserEntity;
import gift.entity.enums.SocialType;
import gift.repository.auth.SocialRepository;
import gift.repository.auth.UserRepository;
import gift.util.api.ApiCall;
import gift.util.auth.ParsingPram;
import gift.util.errorException.BaseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SocialService {

    private final UserRepository userRepository;
    private final SocialRepository socialRepository;
    private final ApiCall apiCall;
    private final ParsingPram parsingPram;

    @Autowired
    public SocialService(UserRepository userRepository,
        SocialRepository socialRepository, ApiCall apiCall, ParsingPram parsingPram) {
        this.userRepository = userRepository;
        this.socialRepository = socialRepository;
        this.apiCall = apiCall;
        this.parsingPram = parsingPram;
    }

    public String getKakoCode(){
        return apiCall.getKakaoCode();
    }

    public kakaoToken getKakaoToken(String code) {
        return apiCall.getKakaoToken(code);
    }

    @Transactional
    public Long SetToKakao(HttpServletRequest req, kakaoToken token) {
        //        유저 검증
        UserEntity user = userRepository.findByIdAndIsDelete(parsingPram.getId(req), 0).orElseThrow(
            () -> new BaseHandler(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.")
        );

        kakaoInfo info = apiCall.getKakaoTokenInfo(token.getAccess_token());
        //        이미 소셜 로그인이 등록된 경우
        if (socialRepository.findBySocialIdAndType(info.getId(), SocialType.KAKAO).isPresent()) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "카카오 소셜로그인이 이미 등록되어 있습니다.");
        }

        //      등록
        SocialEntity socialLogin = new SocialEntity(info.getId(), SocialType.KAKAO, user);
        socialRepository.save(socialLogin);

        return socialLogin.getId();
    }
}
