package gift.product.controller.wish;

import gift.product.dto.auth.LoginMemberIdDto;
import gift.product.dto.wish.WishDto;
import gift.product.model.Wish;
import gift.product.service.WishService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "wish", description = "위시 리스트 관련 API")
@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<List<Wish>> getWishAll(HttpServletRequest request) {
        LoginMemberIdDto loginMemberIdDto = getLoginMember(request);
        List<Wish> wishAll = wishService.getWishAll(loginMemberIdDto);
        return ResponseEntity.ok(wishAll);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Wish>> getWishAll(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5") int size,
        @RequestParam(name = "sort", defaultValue = "id") String sort,
        @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<Wish> wishes = wishService.getWishAll(pageable);
        return ResponseEntity.ok(wishes);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Wish.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Wish> getWish(@PathVariable(name = "id") Long id,
        HttpServletRequest request) {
        LoginMemberIdDto loginMemberIdDto = getLoginMember(request);
        Wish wish = wishService.getWish(id, loginMemberIdDto);
        return ResponseEntity.ok(wish);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Wish.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/insert")
    public ResponseEntity<Wish> insertWish(@Valid @RequestBody WishDto wishDto,
        HttpServletRequest request) {
        LoginMemberIdDto loginMemberIdDto = getLoginMember(request);
        Wish responseWish = wishService.insertWish(wishDto, loginMemberIdDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseWish);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWish(@PathVariable(name = "id") Long id,
        HttpServletRequest request) {
        LoginMemberIdDto loginMemberIdDto = getLoginMember(request);
        wishService.deleteWish(id, loginMemberIdDto);

        return ResponseEntity.ok().build();
    }

    private LoginMemberIdDto getLoginMember(HttpServletRequest request) {
        return new LoginMemberIdDto((Long) request.getAttribute("id"));
    }
}
