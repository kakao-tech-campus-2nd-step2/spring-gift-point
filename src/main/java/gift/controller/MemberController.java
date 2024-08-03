package gift.controller;

import gift.anotation.LoginMember;
import gift.domain.Member;
import gift.dto.AddPointsDTO;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@Tag(name = "MemberController", description = "회원 관리 API")
public class MemberController {
    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    public MemberController(MemberService memberService, ObjectMapper objectMapper) {
        this.memberService = memberService;
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "회원가입", description = "회원가입 수행")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Member member) {
        memberService.register(member);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "User registered successfully");

        String jsonResponse;
        try {
            jsonResponse = objectMapper.writeValueAsString(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating JSON response");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }

    @Operation(summary = "회원 로그인", description = "회원 로그인 수행")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Member member) {
        String token = memberService.login(member.getEmail(), member.getPassword());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);

        String jsonResponse;
        try {
            jsonResponse = objectMapper.writeValueAsString(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating JSON response");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }

    @GetMapping("/point")
    public ResponseEntity<Map<String, Integer>> getPoint(@LoginMember Member member) {
        int points = member.getPoints();
        Map<String, Integer> response = new HashMap<>();
        response.put("point", points);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/point")
    public ResponseEntity<Map<String, Integer>> usePoint(@LoginMember Member member, @RequestBody Map<String, Integer> request) {
        int pointsToUse = request.get("point");
        int currentPoints = member.getPoints();

        if (currentPoints < 1000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", -1));
        }

        if (pointsToUse > currentPoints) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", -1));
        }

        member.setPoints(currentPoints - pointsToUse);
        memberService.save(member);

        return ResponseEntity.ok(Map.of("remainingPoints", member.getPoints()));
    }

    @PostMapping("/addPoints")
    public ResponseEntity<Void> addPoints(@RequestBody AddPointsDTO addPointsDTO) {
        memberService.addPoints(addPointsDTO.getMemberId(), addPointsDTO.getPoints());
        return ResponseEntity.ok().build();
    }
}
