package gift.controller;

import gift.dto.MemberResponseDto;
import gift.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiMemberListController {
    MemberService memberService;

    public ApiMemberListController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/user/list")
    public ResponseEntity<List<MemberResponseDto>> UserList() {
        List<MemberResponseDto> memberResponseDto = memberService.getAllMemberResponseDto();
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }
}
