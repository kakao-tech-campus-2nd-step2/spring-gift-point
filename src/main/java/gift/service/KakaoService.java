package gift.service;

import gift.domain.KakaoLogin;
import gift.dto.KakaoTokenResponse;
import gift.dto.KakaoUserInfo;
import gift.entity.Member;
import gift.jwt.JwtUtil;
import gift.repository.KakaoTokenRepository;
import gift.repository.MemberJpaDao;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class KakaoService {

    private final MemberJpaDao memberJpaDao;
    private final JwtUtil jwtUtil;
    private final KakaoLogin kakaoLogin;

    public KakaoService(MemberJpaDao memberJpaDao, JwtUtil jwtUtil, KakaoLogin kakaoLogin) {
        this.memberJpaDao = memberJpaDao;
        this.jwtUtil = jwtUtil;
        this.kakaoLogin = kakaoLogin;
    }

    public String kakaoLogin(String code) {
        KakaoTokenResponse kakaoToken = kakaoLogin.getKakaoToken(code);
        KakaoUserInfo userInfo = kakaoLogin.getUserInfo(kakaoToken);
        Member kakaoMember = kakaoLogin.generateMemberByKakaoUserInfo(userInfo);

        Optional<Member> queriedMember = memberJpaDao.findByEmail(kakaoMember.getEmail());
        if (queriedMember.isEmpty()) {
            memberJpaDao.save(kakaoMember);
        }
        KakaoTokenRepository.saveAccessToken(kakaoMember.getEmail(), kakaoToken.getAccessToken());

        return jwtUtil.createJwt(kakaoMember.getEmail(), 1000 * 60 * 30);
    }
}
