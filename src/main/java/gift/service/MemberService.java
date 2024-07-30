package gift.service;

import gift.authorization.JwtUtil;
import gift.dto.request.KakaoMemberRequestDTO;
import gift.dto.request.LoginMemberDTO;
import gift.dto.request.NormalMemberRequestDTO;
import gift.dto.request.TokenLoginRequestDTO;
import gift.entity.Member;
import gift.exception.DuplicateValueException;
import gift.exception.memberException.KakaoMemberSignUpException;
import gift.exception.memberException.MemberEmailNotFoundException;
import gift.exception.memberException.NormalMemberSignUpException;
import gift.repository.MemberRepository;
import io.jsonwebtoken.JwtException;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Description("normal member signup")
    public String addMember(NormalMemberRequestDTO normalMemberRequestDTO) {
        String email = normalMemberRequestDTO.email();
        if (memberRepository.existsByEmail(email)) {
            throw new NormalMemberSignUpException("존재하는 email입니다.");
        }
        Member member = toEntity(normalMemberRequestDTO);
        memberRepository.save(member);
        String token = jwtUtil.generateToken(member);
        return token;
    }

    public String addKakaoMember(KakaoMemberRequestDTO kakaoMemberRequestDTO) {
        String kakaoEmail = kakaoMemberRequestDTO.email();
        if (memberRepository.existsByEmail(kakaoEmail)) {
            throw new KakaoMemberSignUpException("존재하는 email입니다");
        }
        Member member = toEntity(kakaoMemberRequestDTO);
        memberRepository.save(member);
        String token = jwtUtil.generateToken(member);
        return token;

    }


    public String login(NormalMemberRequestDTO normalMemberRequestDTO) {
        String email = normalMemberRequestDTO.email();
        String password = normalMemberRequestDTO.password();
        Member existingMember = memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new NormalMemberSignUpException("ID 혹은 비밀번호를 다시 확인하세요."));
        String token = jwtUtil.generateToken(existingMember);
        return token;
    }

    public void tokenLogin(LoginMemberDTO loginMemberDTO) {
        if (jwtUtil.isNotValidToken(loginMemberDTO)) {
            throw new JwtException("service - 토큰 인증이 불가능합니다");
        }
    }


    @Description("임시 확인용 service")
    public List<Member> getAllUsers() {
        return memberRepository.findAll();
    }


    private Member getMemberEntity(NormalMemberRequestDTO normalMemberRequestDTO) {
        String email = normalMemberRequestDTO.email();
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberEmailNotFoundException(email + "을 찾을 수 없습니다."));
    }


    private Member toEntity(NormalMemberRequestDTO normalMemberRequestDTO) {
        return new Member(
                normalMemberRequestDTO.email(),
                normalMemberRequestDTO.password(),
                normalMemberRequestDTO.memberType()
        );
    }

    private Member toEntity(KakaoMemberRequestDTO kakaoMemberRequestDTO) {
        return new Member(
                kakaoMemberRequestDTO.email(),
                kakaoMemberRequestDTO.memberType()
        );
    }



}





