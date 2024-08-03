package gift.services;


import gift.JWTUtil;
import gift.classes.Exceptions.EmailAlreadyExistsException;
import gift.domain.Member;
import gift.dto.MemberDto;
import gift.dto.RequestMemberDto;
import gift.dto.TokenDto;
import gift.repositories.MemberRepository;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;
    private final long currentMemberId = 1;

    @Autowired
    public MemberService(MemberRepository memberRepository, JWTUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public void register(RequestMemberDto requestMemberDto) throws EmailAlreadyExistsException {

        Member member = new Member(
            null,
            requestMemberDto.getEmail(),
            requestMemberDto.getPassword(),
            requestMemberDto.getRole()
        );
        // 이메일로 기존 회원 조회
        Member existingMember = memberRepository.findByEmail(member.getEmail());

        // 이미 존재하는 이메일일 경우 예외 발생
        if (existingMember != null) {
            throw new EmailAlreadyExistsException();
        }

        // 새로운 회원 생성 및 저장
        Member newmember = new Member(
            null,
            requestMemberDto.getEmail(),
            requestMemberDto.getPassword(),
            requestMemberDto.getRole()
        );

        memberRepository.save(newmember);
    }

    public TokenDto login(RequestMemberDto requestMemberDto) {

        // 이메일로 회원 조회
        Member existingMember = memberRepository.findByEmail(requestMemberDto.getEmail());

        // 회원이 존재하지 않거나 비밀번호가 일치하지 않을 경우 예외 발생
        if (existingMember == null || !existingMember.getPassword()
            .equals(requestMemberDto.getPassword())) {
            throw new NoSuchElementException("이메일이 존재하지 않거나 비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성 및 반환
        String token = jwtUtil.createJwt(existingMember.getEmail());
        return new TokenDto(token);

    }

    public MemberDto getLoginUser(String token) {
        String email = jwtUtil.getLoginEmail(token);
        Member existingMember = memberRepository.findByEmail(email);
        MemberDto memberDto = new MemberDto(existingMember.getId(),
            existingMember.getEmail(),
            existingMember.getPassword(),
            existingMember.getRole());
        return memberDto;
    }

}
