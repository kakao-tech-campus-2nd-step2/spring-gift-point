package gift.service;

import gift.dto.KakaoUserInfoDTO;
import gift.dto.MemberDTO;
import gift.entity.Member;
import gift.entity.SnsMember;
import gift.repository.MemberRepository;
import gift.repository.SnsMemberRepository;
import gift.util.KakaoApiUtil;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginService {

    KakaoApiUtil kakaoApiUtil;
    SnsMemberRepository snsMemberRepository;
    MemberService memberService;

    public LoginService(KakaoApiUtil kakaoApiUtil, SnsMemberRepository snsMemberRepository,
        MemberService memberService) {
        this.kakaoApiUtil = kakaoApiUtil;
        this.snsMemberRepository = snsMemberRepository;
        this.memberService = memberService;
    }

    public String oauthLogin(String code) {
        String kakaoAccessToken = kakaoApiUtil.getAccessToken(code);
        KakaoUserInfoDTO kakaoUser = kakaoApiUtil.getUserInfo(kakaoAccessToken);

        return getKakaoUserByOauthId(kakaoUser);
    }

    //DB에 기존에 로그인 이력을 확인 후 첫 로그인이라면 Member 생성, member에 대한 accessToken 생성 후 반환
    private String getKakaoUserByOauthId(KakaoUserInfoDTO kakaoUser) {
        System.out.println(kakaoUser.getOauthId());
        Optional<SnsMember> findKakaoUser = snsMemberRepository.findByOauthId(
            kakaoUser.getOauthId());

        //로그인 이력 있을 경우
        if (findKakaoUser.isPresent()) {
            Member member = findKakaoUser.get().getMember();
            return memberService.authenticateUser(member);
        }

        //로그인 이력이 없을 경우 - 새 Member, SnsMember 생성
        Member newMember = new MemberDTO(kakaoUser.getEmail(),
            UUID.randomUUID().toString()).toEntity();
        memberService.generateUser(newMember);
        snsMemberRepository.save(
            new SnsMember(kakaoUser.getOauthId(), kakaoUser.getEmail(),kakaoUser.getKakaoAccessToken(),newMember));
        return memberService.authenticateUser(newMember);
    }
}
