package gift.service;

import gift.config.KakaoProperties;
import gift.model.entity.Member;
import gift.model.entity.Point;
import gift.model.kakao.KakaoAuth;
import gift.model.kakao.KakaoMember;
import gift.repository.MemberRepository;
import gift.repository.PointRepository;
import gift.service.intercptor.ClientInterceptor;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class KakaoAuthService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoAuthService.class);

    private final KakaoApi kakaoApi;
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;

    public KakaoAuthService(KakaoApi kakaoApi, MemberRepository memberRepository, PointRepository pointRepository) {
        this.kakaoApi = kakaoApi;
        this.memberRepository = memberRepository;
        this.pointRepository = pointRepository;
    }


    public String getKakaoToken(String code){
        var response = kakaoApi.getKakaoToken(code);
        logger.info("getKakaoToekon" + response);

        //email 카카오 아이디로
        Long memberid = getKakakoMemberId(response.getAccessToken());
        if(!memberRepository.existsByEmail(memberid.toString())){
            signIn(memberid);
        }

        return response.getAccessToken();
    }

    private void signIn(Long memberid) {
        Member member = new Member(memberid.toString(), "password");
        memberRepository.save(member);

        memberRepository.findByEmail(memberid.toString()).ifPresent(newMember -> {
            Point point = new Point(newMember, 0);
            pointRepository.save(point);
        });
    }

    public Member getDBMemberByToken(String token){
        Long dbMemberId = getKakakoMemberId(token);
        return memberRepository.findByEmail(dbMemberId.toString())
                .orElseThrow(() -> new NoSuchElementException("해당 멤버가 없습니다."));
    }

    public Long getKakakoMemberId(String token){
        var response = kakaoApi.getKakaoMemberId(token);
        logger.info("getKakaoMeberId{}", response);
        return response.getId();
    }
}
