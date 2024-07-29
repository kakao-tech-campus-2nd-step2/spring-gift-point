package gift.service;

import gift.DTO.User.UserRequest;
import gift.DTO.User.UserResponse;
import gift.domain.User;
import gift.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("saveTest")
    void test1(){
        // given
        ArgumentCaptor<User> captor_u = ArgumentCaptor.forClass(User.class);

        UserRequest userRequest = new UserRequest("userId", "user1@email.com", "aaaaa");
        given(userRepository.save(captor_u.capture())).willAnswer(invocation -> captor_u.getValue());
        // when
        UserResponse answer = userService.save(userRequest);
        // then
        Assertions.assertThat(answer.getUserId()).isEqualTo(userRequest.getUserId());
        Assertions.assertThat(answer.getEmail()).isEqualTo(userRequest.getEmail());
        Assertions.assertThat(answer.getPassword()).isEqualTo(userRequest.getPassword());
    }

    @Test
    @DisplayName("findAllASCTest")
    void test2(){
        // given
        List<User> users = new ArrayList<>();
        for(int i = 1; i <= 5; i++){
            User user = new User("user" + i, "user" + i + "@email.com", "" + i);
            users.add(user);
        }

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("name"));
        Pageable pageable = PageRequest.of(0, 5, Sort.by(sorts));
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), users.size());
        List<User> subList = users.subList(start, end);

        Page<User> page = new PageImpl<>(subList, pageable, users.size());
        given(userRepository.findAll(pageable)).willAnswer(invocation -> page);
        // when
        Page<UserResponse> pageResult = userService.findAllASC(0, 5, "name");
        // then
        Assertions.assertThat(pageResult).isNotNull();
        Assertions.assertThat(pageResult.get().count()).isEqualTo(5);
        List<UserResponse> content = pageResult.getContent();
        for(int i = 1; i <= 5; i++){
            String userId = content.get(i-1).getUserId();
            Assertions.assertThat(userId).isEqualTo("user"+i);
        }
    }

    @Test
    @DisplayName("findAllDESCTest")
    void test3(){
        // given
        List<User> users = new ArrayList<>();
        for(int i = 1; i <= 5; i++){
            User user = new User("user" + i, "user" + i + "@email.com", "" + i);
            users.add(user);
        }

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("name"));
        Pageable pageable = PageRequest.of(0, 5, Sort.by(sorts));
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), users.size());
        List<User> subList = users.subList(start, end);

        Page<User> page = new PageImpl<>(subList, pageable, users.size());
        given(userRepository.findAll(pageable)).willAnswer(invocation -> page);
        // when
        Page<UserResponse> pageResult = userService.findAllDESC(0, 5, "name");
        // then
        Assertions.assertThat(pageResult).isNotNull();
        Assertions.assertThat(pageResult.get().count()).isEqualTo(5);
        List<UserResponse> content = pageResult.getContent();
        for(int i = 5; i >= 1; i--){
            String userId = content.get(i-1).getUserId();
            Assertions.assertThat(userId).isEqualTo("user"+i);
        }
    }

    @Test
    @DisplayName("findByUserIdTest")
    void test4(){
        // given
        User user = new User("userId", "user1@email.com", "aaaaa");
        given(userRepository.findByUserId("userId")).willAnswer(invocation -> user);
        // when
        UserResponse userResponse = userService.findByUserId("userId");
        // given
        Assertions.assertThat(userResponse.getUserId()).isEqualTo("userId");
        Assertions.assertThat(userResponse.getEmail()).isEqualTo("user1@email.com");
        Assertions.assertThat(userResponse.getPassword()).isEqualTo("aaaaa");
    }

    @Test
    @DisplayName("findByIdTest")
    void test5(){
        // given
        User user = new User("userId", "user1@email.com", "aaaaa");
        given(userRepository.findById(1L)).willAnswer(invocation -> Optional.of(user));
        // when
        UserResponse userResponse = userService.findById(1L);
        // given
        Assertions.assertThat(userResponse.getUserId()).isEqualTo("userId");
        Assertions.assertThat(userResponse.getEmail()).isEqualTo("user1@email.com");
        Assertions.assertThat(userResponse.getPassword()).isEqualTo("aaaaa");
    }

    @Test
    @DisplayName("updateTest")
    void test6(){
        // given
        UserRequest userRequest = new UserRequest("userId", "user2@email.com", "aaaab");

        User user = new User("userId", "user1@email.com", "aaaaa");
        given(userRepository.findByUserId("userId")).willAnswer(invocation -> user);
        // when
        userService.update(userRequest);
        // then
        Assertions.assertThat(user.getUserId()).isEqualTo("userId");
        Assertions.assertThat(user.getEmail()).isEqualTo("user2@email.com");
        Assertions.assertThat(user.getPassword()).isEqualTo("aaaab");
    }

    @Test
    @DisplayName("deleteTest")
    void test7(){
        // when
        userService.delete(1L);
        // then
        then(userRepository).should(times(1)).deleteById(1L);
    }
}
