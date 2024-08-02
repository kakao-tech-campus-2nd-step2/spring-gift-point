package gift.controller;

import gift.domain.AuthToken;
import gift.dto.request.PointRequestDto;
import gift.dto.response.MemberResponseDto;
import gift.service.AuthService;
import gift.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/point")
public class PointController {

    private final AuthService authService;
    private final TokenService tokenService;

    public PointController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Integer>> getPoint(HttpServletRequest request){
        AuthToken token = getAuthVO(request);
        MemberResponseDto findMember = authService.findOneByEmail(token.getEmail());

        Map<String, Integer> response = new HashMap<>();
        response.put("point", findMember.point());

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Integer>> addPoint(HttpServletRequest request,
                                                         @RequestBody @Valid PointRequestDto pointRequestDto){
        AuthToken token = getAuthVO(request);
        MemberResponseDto findMember = authService.addPoint(token.getEmail(), pointRequestDto.point());

        Map<String, Integer> response = new HashMap<>();
        response.put("point", findMember.point());

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    public AuthToken getAuthVO(HttpServletRequest request) {
        String key = request.getHeader("Authorization").substring(7);
        AuthToken token = tokenService.findToken(key);
        return token;
    }
}
