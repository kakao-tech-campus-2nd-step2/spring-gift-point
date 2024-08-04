package gift.service;

import gift.domain.AuthToken;
import gift.domain.Member;
import gift.domain.TokenInformation;
import gift.dto.request.MemberRequestDto;
import gift.dto.response.MemberResponseDto;
import gift.exception.customException.EmailDuplicationException;
import gift.exception.customException.MemberNotFoundException;
import gift.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    public static final String KAKAO_COM = "@kakao.com";
    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    public AuthService(MemberRepository memberRepository, TokenService tokenService) {
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public MemberResponseDto memberJoin(MemberRequestDto memberRequestDto) {
        Member member = new Member.Builder()
                .email(memberRequestDto.email())
                .password(memberRequestDto.password())
                .build();

        Optional<Member> memberByEmail = memberRepository.findMemberByEmail(memberRequestDto.email());

        if (memberByEmail.isPresent()) {
            throw new EmailDuplicationException();
        }

        Member savedMember = memberRepository.save(member);

        return MemberResponseDto.from(savedMember);
    }

    @Transactional
    public String kakaoMemberLogin(String kakaoId, TokenInformation tokenInfo) {
        MemberResponseDto memberResponseDto = memberRepository.findMemberByKakaoId(kakaoId).map(MemberResponseDto::from)
                .orElseGet(() -> memberKakaoJoin(kakaoId));

        AuthToken authToken = tokenService.oauthTokenSave(tokenInfo, memberResponseDto.email());

        return authToken.getToken();
    }

    @Transactional
    public MemberResponseDto memberKakaoJoin(String kakaoId) {
        Member member = new Member.Builder()
                .email(kakaoId + KAKAO_COM)
                .password(kakaoId + KAKAO_COM)
                .kakaoId(kakaoId)
                .build();

        Member savedMember = memberRepository.save(member);

        return MemberResponseDto.from(savedMember);
    }

    public MemberResponseDto findOneByEmailAndPassword(MemberRequestDto memberRequestDto) {
        Member findMember = memberRepository.findMemberByEmailAndPassword(memberRequestDto.email(), memberRequestDto.password())
                .orElseThrow(MemberNotFoundException::new);

        return MemberResponseDto.from(findMember);
    }

    public MemberResponseDto findOneByEmail(String email) {
        Member findMember = memberRepository.findMemberByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        return MemberResponseDto.from(findMember);
    }

    @Transactional
    public MemberResponseDto addPoint(String email, int point) {
        Member findMember = memberRepository.findMemberByEmailForUpdate(email)
                .orElseThrow(MemberNotFoundException::new);

        findMember.addPoint(point);

        return MemberResponseDto.from(findMember);
    }
}
