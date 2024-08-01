package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.config.KakaoAuthClient;
import gift.config.KakaoUserClinet;
import gift.dto.kakaoDto.KakaoInfoDto;
import gift.dto.kakaoDto.KakaoTokenResponseDto;
import gift.dto.memberDto.MemberDto;
import gift.model.member.Member;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;


import java.security.SecureRandom;
import java.util.Optional;

@Service
public class KakaoService {

    private final MemberRepository memberRepository;

    private final KakaoAuthClient kakaoAuthClient;

    private final KakaoUserClinet kakaoUserClinet;

    public KakaoService(KakaoAuthClient kakaoAuthClient,MemberRepository memberRepository, KakaoUserClinet kakaoUserClinet) {
        this.kakaoAuthClient = kakaoAuthClient;
        this.memberRepository = memberRepository;
        this.kakaoUserClinet = kakaoUserClinet;
    }

    public String getAccessTokenFromKakao(String code) {
        KakaoTokenResponseDto kakaoTokenResponseDto = kakaoAuthClient.getAccessToken(code).block();
        return kakaoTokenResponseDto.accessToken();
    }

    public KakaoInfoDto getUserInfo(String accessToken) throws JsonProcessingException {
        KakaoInfoDto kakaoInfoDto = kakaoUserClinet.getUserInfo(accessToken);
        return kakaoInfoDto;
    }

    public MemberDto registerOrGetKakaoMember(KakaoInfoDto kakaoInfoDto){
        String email = kakaoInfoDto.getId() + "kakao@naver.com";
        Optional<Member> kakaoMember = memberRepository.findByEmail(email);

        if (kakaoMember.isPresent()) {
            Member existingMember = kakaoMember.get();
            return new MemberDto(existingMember.getEmail(), existingMember.getPassword());
        }
        String tempPassword = new SecureRandom().toString();
        Member newKakaoMember = new Member(email, tempPassword);
        memberRepository.save(newKakaoMember);
        return new MemberDto(newKakaoMember.getEmail(), newKakaoMember.getPassword());
    }

    public void kakaoDisconnect(String accessToken){
        kakaoAuthClient.kakaoDisconnect(accessToken);
    }
}