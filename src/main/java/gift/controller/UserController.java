package gift.controller;

import gift.exception.AlreadyExistException;
import gift.DTO.Token;
import gift.DTO.User.UserRequest;
import gift.DTO.User.UserResponse;
import gift.exception.LogicalException;
import gift.security.AuthenticateMember;
import gift.security.JwtTokenProvider;
import gift.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider){
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    /*
     * 로그인 ( 유저 정보 인증 )
     */
    @PostMapping("/login")
    public ResponseEntity<Token> giveToken(@RequestBody UserRequest user) throws IllegalAccessException {
        if(!userService.login(user)){
            throw new IllegalArgumentException("아이디나 비밀번호를 다시 확인해주세요!");
        }

        Token token = jwtTokenProvider.makeToken(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
    /*
     * 회원가입 ( 유저 추가 )
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserRequest user){
        if(userService.isUserIdDuplicate(user.getUserId()))
            throw new AlreadyExistException("이미 존재하는 유저 아이디입니다!");

        if(userService.isEmailDuplicate(user.getEmail()))
            throw new AlreadyExistException("이미 존재하는 이메일입니다!");

        userService.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    /*
     * 유저 조회
     */
    @GetMapping("/api/users")
    public ResponseEntity<Page<UserResponse>> readUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sort", defaultValue = "asc") String sort,
            @RequestParam(value = "field", defaultValue = "id") String field
    ) {
        if(sort.equals("asc")) {
            Page<UserResponse> users = userService.findAllASC(page, size,field);
            return new ResponseEntity<>(users, HttpStatus.OK);
        }

        Page<UserResponse> users = userService.findAllDESC(page, size,field);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    /*
     * 유저 수정
     */
    @PutMapping("/api/users/{id}")
    public ResponseEntity<Void> updateUsers(
            @PathVariable("id") Long id,
            @RequestBody UserRequest user,
            @AuthenticateMember UserResponse userRes
    ) throws NoSuchFieldException {
        if(!id.equals(userService.findByUserId(user.getUserId()).getId())) {
            throw new LogicalException("리소스 접근 URI와 요청 데이터가 다릅니다!");
        }

        if(!userService.isUserIdDuplicate(user.getUserId())) {
            throw new NoSuchFieldException("존재하지 않는 유저 정보 접근입니다!");
        }

        userService.update(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    /*
     * 유저 삭제
     */
    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<Void> deleteUsers(
            @PathVariable("id") Long id,
            @AuthenticateMember UserResponse user
    ){
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
