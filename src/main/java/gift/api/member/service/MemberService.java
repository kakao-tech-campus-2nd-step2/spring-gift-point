package gift.api.member.service;

import gift.api.member.domain.Member;
import gift.api.member.dto.KakaoAccount;
import gift.api.member.dto.MemberRequest;
import gift.api.member.exception.EmailAgreementNeededException;
import gift.api.member.exception.EmailAlreadyExistsException;
import gift.api.member.exception.RegisterNeededException;
import gift.api.member.repository.MemberRepository;
import gift.global.exception.ForbiddenMemberException;
import gift.global.exception.NoSuchEntityException;
import gift.global.exception.UnauthorizedMemberException;
import gift.global.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long register(MemberRequest memberRequest) {
        if (memberRepository.existsByEmail(memberRequest.email())) {
            throw new EmailAlreadyExistsException();
        }
        return memberRepository.save(memberRequest.toEntity()).getId();
    }

    public void login(MemberRequest memberRequest, String token) {
        if (memberRepository.existsByEmailAndPassword(memberRequest.email(), memberRequest.password())) {
            Long id = findMemberByEmail(memberRequest.email()).getId();
            if (token.equals(JwtUtil.generateAccessToken(id, memberRequest.email(), memberRequest.role()))) {
                return;
            }
            throw new UnauthorizedMemberException();
        }
        throw new ForbiddenMemberException();
    }

    public void verifyEmail(KakaoAccount kakaoAccount) {
        if (kakaoAccount.emailNeedsAgreement()) {
            throw new EmailAgreementNeededException();
        }
        if (kakaoAccount.isEmailValid()) {
            if (!memberRepository.existsByEmail(kakaoAccount.email())) {
                throw new RegisterNeededException();
            }
        }
    }

    @Transactional
    public String saveKakaoToken(String email, String accessToken) {
        Member member = findMemberByEmail(email);
        member.saveKakaoToken(accessToken);
        return accessToken;
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new NoSuchEntityException("member"));
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new NoSuchEntityException("member"));
    }
}
