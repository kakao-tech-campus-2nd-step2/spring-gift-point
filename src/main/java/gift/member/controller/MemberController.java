package gift.member.controller;

import gift.global.exception.DomainValidationException;
import gift.global.response.ErrorResponseDto;
import gift.global.response.ResultCode;
import gift.global.response.ResultResponseDto;
import gift.global.response.SimpleResultResponseDto;
import gift.global.utils.ResponseHelper;
import gift.member.domain.Member;
import gift.member.dto.MemberRequestDto;
import gift.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("")
    public ResponseEntity<ResultResponseDto<List<Member>>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseHelper.createResponse(ResultCode.GET_ALL_MEMBERS_SUCCESS, members);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponseDto<Member>> getMemberById(@PathVariable(name = "id") Long id) {
        Member member = memberService.getMemberById(id);
        return ResponseHelper.createResponse(ResultCode.GET_MEMBER_BY_ID_SUCCESS, member);
    }

    @PostMapping("")
    public ResponseEntity<SimpleResultResponseDto> createMember(@RequestBody MemberRequestDto memberRequestDTO) {
        memberService.createMember(memberRequestDTO.toMemberServiceDto());
        return ResponseHelper.createSimpleResponse(ResultCode.CREATE_MEMBER_SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> updateMember(@PathVariable(name = "id") Long id, @RequestBody MemberRequestDto memberRequestDTO) {
        memberService.updateMember(memberRequestDTO.toMemberServiceDto(id));
        return ResponseHelper.createSimpleResponse(ResultCode.UPDATE_MEMBER_SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> deleteMember(@PathVariable(name = "id") Long id) {
        memberService.deleteMember(id);
        return ResponseHelper.createSimpleResponse(ResultCode.DELETE_PRODUCT_SUCCESS);
    }

    // GlobalException Handler 에서 처리할 경우,
    // RequestBody에서 발생한 에러가 HttpMessageNotReadableException 로 Wrapping 이 되는 문제가 발생한다
    // 때문에, 해당 에러로 Wrapping 되기 전 Controller 에서 Domain Error 를 처리해주었다
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleOptionValidException(DomainValidationException e) {
        System.out.println(e);
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }
}
