package gift.service;

import gift.entity.MemberEntity;
import gift.dto.MemberDTO;
import gift.repository.MemberRepository;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private KakaoUserService kakaoUserService;

    @Autowired
    public MemberService(MemberRepository memberRepository, KakaoUserService kakaoUserService) {
        this.memberRepository = memberRepository;
        this.kakaoUserService = kakaoUserService;
    }

    public MemberEntity authenticateToken(MemberDTO memberDTO) {
        MemberEntity foundMember = memberRepository.findByEmail(memberDTO.getEmail());

        if (foundMember == null || !memberDTO.getPassword().equals(foundMember.getPassword())) {
            throw new IllegalArgumentException("로그인 정보가 올바르지 않습니다.");
        }

        return foundMember;
    }

    @Transactional
    public MemberEntity registerOrLoginKakaoUser(String accessToken) {
        Map<String, Object> userInfo = kakaoUserService.getKakaoUserInfo(accessToken);

        Long kakaoId = ((Number) userInfo.get("id")).longValue();

        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        String email = (String) kakaoAccount.get("email");

        if (email == null) {
            email = kakaoId + "@kakao.com";
        }

        String password = "randomPassword";

        if (!existsByEmail(email)) {
            save(new MemberDTO(email, password));
        }

        return memberRepository.findByEmail(email);
    }

    @Transactional
    public void save(MemberDTO memberDTO) {
        if (existsByEmail(memberDTO.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // DTO to Entity
        MemberEntity memberEntity = new MemberEntity(memberDTO.getEmail(), memberDTO.getPassword());
        memberRepository.save(memberEntity);
    }


    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
}
