package gift.product.docs;

import gift.product.dto.MemberDTO;
import gift.product.dto.PointResponseDTO;
import gift.product.dto.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Member", description = "API 컨트롤러")
public interface MemberControllerDocs {

    @Operation(summary = "회원 가입", description = "회원 정보를 애플리케이션에 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 가입 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TokenDTO.class))),
        @ApiResponse(responseCode = "400", description = "이메일 형식이 올바르지 않거나 비밀번호가 4자 이상 입력되지 않음"),
        @ApiResponse(responseCode = "409", description = "중복된 이메일에 대한 회원 가입 시도")})
    ResponseEntity<?> signUp(MemberDTO memberDTO, BindingResult bindingResult);

    @Operation(summary = "로그인", description = "애플리케이션의 기능을 이용하기 위한 권한을 취득합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TokenDTO.class))),
        @ApiResponse(responseCode = "400", description = "등록된 회원 정보가 없음")})
    ResponseEntity<?> login (MemberDTO memberDTO, BindingResult bindingResult);

    @Operation(summary = "포인트 조회", description = "유저의 보유 포인트를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "보유 포인트 조회 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PointResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "등록된 회원 정보가 없음"),
        @ApiResponse(responseCode = "401", description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우")})
    ResponseEntity<PointResponseDTO> getPoint(@RequestHeader("Authorization") String authorization);
}
