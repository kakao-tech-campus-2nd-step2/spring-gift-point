package gift.Service;

import gift.DTO.Member;
import gift.DTO.MemberDto;
import gift.LoginUser;
import gift.Repository.MemberRepository;
import gift.ResponseDto.RequestPointDto;
import gift.ResponseDto.ResponsePointDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class PointService {

  private final MemberRepository memberRepository;

  public PointService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  public void addPoint(@RequestBody RequestPointDto requestPointDto,
    @LoginUser MemberDto memberDto) {
    int point = requestPointDto.getPoint();
    Long memberId = memberDto.getId();
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 고객이 없습니다", 1));

    member.addPoint(point);
    memberRepository.save(member);
  }

  public ResponsePointDto getPoint(@LoginUser MemberDto memberDto) {
    Long memberId = memberDto.getId();
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 고객이 없습니다", 1));

    return new ResponsePointDto(member.getPoint());

  }
}
