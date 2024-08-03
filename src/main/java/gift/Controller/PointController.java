package gift.Controller;

import gift.DTO.MemberDto;
import gift.LoginUser;
import gift.ResponseDto.RequestPointDto;
import gift.ResponseDto.ResponsePointDto;
import gift.Service.PointService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/points")
public class PointController {

  private final PointService pointService;

  public PointController(PointService pointService) {
    this.pointService = pointService;
  }

  @PostMapping
  public ResponseEntity addPoint(@RequestBody RequestPointDto requestPointDto,
    @LoginUser MemberDto memberDto) {
    pointService.addPoint(requestPointDto, memberDto);
    return ResponseEntity.created(URI.create("point")).build();
  }

  @GetMapping
  public ResponseEntity<ResponsePointDto> getPoint(@LoginUser MemberDto memberDto) {
    return ResponseEntity.ok(pointService.getPoint(memberDto));
  }
}
