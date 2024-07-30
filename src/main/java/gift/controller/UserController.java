package gift.controller;

import gift.domain.User;
import gift.domain.User.UserSimple;
import gift.entity.UserEntity;
import gift.service.UserService;
import gift.util.page.PageMapper;
import gift.util.page.PageResult;
import gift.util.page.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 관련 서비스")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "유저 리스트 조회")
    @GetMapping
    public PageResult<UserSimple> getUserList(@Valid User.getList param) {
        return PageMapper.toPageResult(userService.getUserList(param));
    }

    @Operation(summary = "단일 유저 조회")
    @GetMapping("/{id}")
    public SingleResult<UserEntity> getUser(@PathVariable long id) {
        return new SingleResult<>(userService.getUser(id));
    }

    @Operation(summary = "유저 생성")
    @PostMapping
    public SingleResult<Long> createUser(@Valid @RequestBody User.CreateUser create) {
        return new SingleResult<>(userService.createUser(create));
    }

    @Operation(summary = "유저 비밀번호 수정")
    @PutMapping("/{id}")
    public SingleResult<Long> updatePassword(@PathVariable long id,
        @Valid @RequestBody User.UpdateUser update) {
        return new SingleResult<>(userService.updatePassword(id, update));
    }

    @Operation(summary = "유저 삭제", description = "soft delete를 수행합니다.")
    @DeleteMapping("/{id}")
    public SingleResult<Long> deleteUser(@PathVariable long id) {
        return new SingleResult<>(userService.deleteUser(id));
    }
}
