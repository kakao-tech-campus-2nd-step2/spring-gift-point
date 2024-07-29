package gift.service;

import gift.dto.LoginRequest;
import gift.dto.LoginResponse;
import gift.dto.MemberRequest;
import gift.exception.AlreadyExistsException;
import gift.exception.user.MemberNotFoundException;
import gift.jwt.JwtUtil;
import gift.model.Member;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginResponse register(MemberRequest request) {
        Optional<Member> optionalUser = memberRepository.findByEmail(request.email());

        if (!optionalUser.isPresent()) {
            Member member = new Member(
                    request.email(),
                    request.password()
            );
            memberRepository.save(member);
            LoginResponse response = new LoginResponse("");
            return response;
        }
        throw new AlreadyExistsException("해당 email의 계정이 이미 존재하는 계정입니다.");
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new MemberNotFoundException("해당 유저가 존재하지 않습니다."));
        if (!member.matchPassword(request.password())) {
            throw new MemberNotFoundException("비밀번호가 일치하지 않습니다.");
        }
        LoginResponse response = new LoginResponse(JwtUtil.createToken(member.getEmail()));
        return response;
    }
}
