package gift.Service;

import gift.DTO.JwtToken;
import gift.DTO.Member;
import gift.DTO.MemberDto;
import gift.Exception.ForbiddenException;
import gift.Exception.UnauthorizedException;
import gift.Repository.MemberRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final JwtService jwtService;

  public MemberService(MemberRepository memberRepository, JwtService jwtService) {
    this.memberRepository = memberRepository;
    this.jwtService = jwtService;
  }

  public MemberDto SignUp(MemberDto memberDtoInfo) {
    Member member = new Member(memberDtoInfo.getId(), memberDtoInfo.getEmail(),
      memberDtoInfo.getPassword());
    memberRepository.save(member);
    return memberDtoInfo;
  }

  public JwtToken Login(MemberDto memberDtoInfo) {
    String email = memberDtoInfo.getEmail();
    Member userByEmail = memberRepository.findByEmail(email)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저가 없습니다.", 1));

    if (userByEmail.matchLoginInfo(memberDtoInfo)) {
      JwtToken jwtToken = jwtService.createAccessToken(memberDtoInfo);
      if (jwtService.isValidToken(jwtToken)) { //토큰이 만료되었다면
        throw new UnauthorizedException("토큰이 유효하지 않습니다.");
      }
      return jwtToken;
    }
    throw new ForbiddenException("아이디 비밀번호가 틀립니다.");
  }
}
