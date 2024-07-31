package gift.service;

import gift.DTO.kakao.KakaoSignupRequest;
import gift.DTO.member.LoginRequest;
import gift.DTO.member.LoginResponse;
import gift.DTO.member.MemberResponse;
import gift.DTO.member.SignupRequest;
import gift.DTO.member.SignupResponse;
import gift.domain.Member;
import gift.exception.member.DuplicatedEmailException;
import gift.exception.member.InvalidAccountException;
import gift.exception.member.PasswordMismatchException;
import gift.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    @Autowired
    public MemberService(MemberRepository memberRepository, TokenService tokenService) {
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public SignupResponse registerMember(SignupRequest signupRequest) {
        memberRepository.findByEmail(signupRequest.getEmail()).ifPresent(p -> {
            throw new DuplicatedEmailException();
        });
        Member member = new Member(signupRequest.getEmail(),
                                    signupRequest.getPassword(),
                                    signupRequest.getKakaoId());
        memberRepository.save(member);

        confirmPassword(signupRequest.getPassword(), signupRequest.getConfirmPassword());

        return new SignupResponse(member.getEmail());
    }

    @Transactional
    public SignupResponse registerKakaoMember(KakaoSignupRequest kakaoSignupRequest) {
        SignupRequest signupRequest = new SignupRequest(kakaoSignupRequest.kakaoId());
        return registerMember(signupRequest);
    }

    @Transactional(readOnly = true)
    public LoginResponse loginMember(LoginRequest loginRequest) {
        Optional<Member> member = memberRepository.findByEmail(loginRequest.getEmail());
        member.orElseThrow(
            () -> new InvalidAccountException());
        Member registeredMember = member.get();

        validatePassword(registeredMember, loginRequest.getPassword());

        String token = tokenService.generateToken(registeredMember);
        return new LoginResponse(token);
    }

    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                                .orElseThrow(() -> new InvalidAccountException());
        return member;
    }

    @Transactional(readOnly = true)
    public Member getMemberByKakaoId(Long kakaoId) {
        return memberRepository.findByKakaoId(kakaoId).orElse(null);

    }

    private void validatePassword(Member member, String password) {
        if (!member.getPassword().equals(password)) {
            throw new InvalidAccountException();
        }
    }

    private void confirmPassword(String password, String passwordConfirm) {
        if(!password.equals(passwordConfirm)) {
            throw new PasswordMismatchException();
        }
    }

    public List<MemberResponse> getMembers() {
        return memberRepository.findAll()
                            .stream()
                            .map(MemberResponse::fromEntity)
                            .toList();
    }
}
