package gift.member.application.service;

import gift.exception.AlreadyExistMember;
import gift.exception.NotFoundMember;
import gift.member.application.dto.PointResponseDto;
import gift.member.util.JwtTokenUtil;
import gift.member.application.dto.TokenResponseDto;
import gift.member.application.dto.RegisterResponseDto;
import gift.member.persistence.Member;
import gift.member.persistence.MemberRepository;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getMemberByEmail(String userEmail) {
        return memberRepository.findByEmail(userEmail).get();
    }

    public RegisterResponseDto register(Member member) throws AlreadyExistMember {
        Optional<Member> existMember = memberRepository.findByEmail(member.getEmail());

        if (!existMember.isPresent()) {
            member.initPoint(10000);
            memberRepository.saveAndFlush(member);
            String token = JwtTokenUtil.generateToken(member.getEmail());
            return new RegisterResponseDto(member.getId(), member.getEmail(), member.getName(), token);
        } else {
            throw new AlreadyExistMember("이미 회원정보가 존재합니다");
        }
    }

    public ResponseEntity<TokenResponseDto> login(Member member) throws NotFoundMember {
        Optional<Member> existMember = memberRepository.findByEmailAndPassword(member.getEmail(),member.getPassword());
        if (existMember.isPresent()) {
            String token = JwtTokenUtil.generateToken(member.getEmail());
            return ResponseEntity.ok(new TokenResponseDto(token));
        } else {
            throw new NotFoundMember("회원정보가 존재하지 않습니다");
        }
    }

    public PointResponseDto getPoint(Member member) {
        return new PointResponseDto(member.getPoint());
    }

}
