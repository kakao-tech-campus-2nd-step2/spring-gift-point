package gift.service;

import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final KakaoService kakaoService;

    @Autowired
    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil, KakaoService kakaoService) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.kakaoService = kakaoService;
    }

    public String kakaoLogin(String code) {
        String accessToken = kakaoService.getAccessToken(code);
        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

        Long kakaoId = (Long) userInfo.get("id");
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String nickname = (String) profile.get("nickname");

        Optional<Member> optionalMember = memberRepository.findByKakaoId(kakaoId);
        Member member;
        if (optionalMember.isPresent()) {
            member = optionalMember.get();
            member.setKakaoToken(accessToken);
        } else {
            member = new Member(kakaoId, nickname, accessToken);
            memberRepository.save(member);
        }

        return jwtUtil.generateToken(member.getId(), member.getNickname(), "USER");
    }
}
