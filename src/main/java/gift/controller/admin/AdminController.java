package gift.controller.admin;

import gift.dto.paging.PagingRequest;
import gift.dto.paging.PagingResponse;
import gift.dto.point.MemberPointRequest;
import gift.dto.point.MemberPointResponse;
import gift.dto.user.UserResponse;
import gift.model.user.User;
import gift.service.admin.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/members")
    public ResponseEntity<PagingResponse<UserResponse.Info>> getUserList(@RequestAttribute("user") User user,
                                                                         @ModelAttribute PagingRequest pagingRequest) {
        PagingResponse<UserResponse.Info> userList = adminService.getUserList(user, pagingRequest.getPage(), pagingRequest.getSize());
        return ResponseEntity.ok(userList);

    }

    @PatchMapping("/members/{memberId}/point")
    public ResponseEntity<MemberPointResponse.Info> addPointToUser(@RequestAttribute("user") User user,
                                                                   @PathVariable("memberId") Long userId,
                                                                   @Valid @RequestBody MemberPointRequest.Add memberPointRequest) {
        MemberPointResponse.Info info = adminService.addPointToUser(user, memberPointRequest.depositPoint(), userId);
        return ResponseEntity.ok(info);

    }

}
