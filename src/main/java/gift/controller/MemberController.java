package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.MemberLoginDto;
import gift.dto.MemberRegisterDto;
import gift.entity.TokenResponseEntity;
import gift.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> signUp(@RequestBody MemberLoginDto memberLoginDto)
        throws Exception {
        String generatedToken = memberService.registerMember(memberLoginDto.email,
            memberLoginDto.password);
        return new ResponseEntity<>(
            objectMapper.writeValueAsString(new TokenResponseEntity(generatedToken)),
            HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> signIn(@RequestBody MemberRegisterDto memberRegisterDto)
        throws Exception {
        String generatedToken = memberService.authenticateMember(memberRegisterDto.email,
            memberRegisterDto.password);
        return new ResponseEntity<>(
            objectMapper.writeValueAsString(new TokenResponseEntity(generatedToken)),
            HttpStatus.OK);
    }
}
