package gift.docs.member;

import gift.member.business.dto.JwtToken;
import gift.member.presentation.dto.RequestMemberDto;
import gift.member.presentation.dto.RequestWishlistDto;
import gift.member.presentation.dto.ResponsePagingWishlistDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "Member, Wishlist", description = "회원, 위시리스트 관련 API")
public interface MemberApiDocs {

    @Operation(summary = "회원 가입", security = {})
    ResponseEntity<JwtToken> registerMember(
        RequestMemberDto requestMemberDto);

    @Operation(summary = "로그인", security = {})
    ResponseEntity<JwtToken> loginMember(RequestMemberDto requestMemberDto);

    @Operation(summary = "토큰 재발급", security = {})
    ResponseEntity<String> reissueRefreshToken(String refreshToken);

    @Operation(summary = "위시리스트 페이지로 조회")
    ResponseEntity<ResponsePagingWishlistDto> getWishlistsByPage(
        @Parameter(hidden = true) Long memberId,
        Pageable pageable,
        @Parameter(hidden = true) Integer size);

    @Operation(summary = "위시리스트 추가")
    ResponseEntity<Long> addWishList(
        @Parameter(hidden = true) Long memberId,
        Long productId);

    @Operation(summary = "위시리스트 수정")
    ResponseEntity<Long> updateWishList(
        @Parameter(hidden = true) Long memberId,
        Long productId,
        RequestWishlistDto requestWishlistDto);

    @Operation(summary = "위시리스트 삭제")
    ResponseEntity<Void> deleteWishList(
        @Parameter(hidden = true) Long memberId,
        Long productId);

}
