package gift.service.impl;

import gift.model.AccessToken;
import gift.model.Member;
import gift.repository.AccessTokenRepository;
import gift.repository.MemberRepository;
import gift.service.UserService;
import gift.dto.UserInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final MemberRepository memberRepository;
    private final AccessTokenRepository accessTokenRepository;

    public UserServiceImpl(MemberRepository memberRepository, AccessTokenRepository accessTokenRepository) {
        this.memberRepository = memberRepository;
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public void saveUser(String accessToken, UserInfo userInfo) {
        Optional<Member> memberOptional = memberRepository.findByEmail(userInfo.getEmail());
        Member member;
        if (memberOptional.isPresent()) {
            member = memberOptional.get();
        }
        else {
            member = new Member(userInfo.getEmail(), null, "kakao");
            memberRepository.save(member);
        }

        AccessToken token = new AccessToken(member, "kakao", accessToken, LocalDateTime.now().plusHours(1));
        accessTokenRepository.save(token);
    }
}
