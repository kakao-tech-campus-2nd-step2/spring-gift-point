package gift.service;

import gift.authentication.token.JwtProvider;
import gift.authentication.token.Token;
import gift.domain.Member;
import gift.domain.vo.Email;
import gift.repository.MemberRepository;
import gift.repository.WishProductRepository;
import gift.web.client.dto.KakaoAccount;
import gift.web.dto.request.LoginRequest;
import gift.web.dto.request.member.CreateMemberRequest;
import gift.web.dto.response.LoginResponse;
import gift.web.dto.response.member.CreateMemberResponse;
import gift.web.dto.response.member.ReadMemberResponse;
import gift.web.validation.exception.client.IncorrectEmailException;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final WishProductRepository wishProductRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, WishProductRepository wishProductRepository,
        JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.wishProductRepository = wishProductRepository;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public CreateMemberResponse createMember(CreateMemberRequest request) {
        Member member = request.toEntity();
        Member savedMember = memberRepository.save(member);

        Token token = jwtProvider.generateToken(savedMember);

        return CreateMemberResponse.of(savedMember, token);
    }

    public ReadMemberResponse readMember(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id: " + id));

        return ReadMemberResponse.fromEntity(member);
    }

    public Member findOrCreateMember(KakaoAccount kakaoAccount) {
        String email = kakaoAccount.getEmail();
        return memberRepository.findByEmail(Email.from(email))
            .orElseGet(() -> memberRepository.save(kakaoAccount.toMember()));
    }

    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NoSuchElementException::new);
        wishProductRepository.deleteAllByMemberId(id);
        memberRepository.delete(member);
    }

    public LoginResponse login(LoginRequest request) {
        Email email = Email.from(request.getEmail());
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IncorrectEmailException(email.getValue()));

        member.matchPassword(request.getPassword());

        Token token = jwtProvider.generateToken(member);

        return new LoginResponse(token.getValue());
    }
}
