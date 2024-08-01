package gift.service.admin;

import gift.common.enums.Role;
import gift.dto.paging.PagingResponse;
import gift.dto.point.MemberPointRequest;
import gift.dto.point.MemberPointResponse;
import gift.dto.product.ProductResponse;
import gift.dto.user.UserResponse;
import gift.exception.UserNotFoundException;
import gift.model.user.User;
import gift.repository.user.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PagingResponse<UserResponse.Info> getUserList(User user, int page, int size){
        user.isAdmin();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<User> users = userRepository.findAll(pageRequest);
        List<UserResponse.Info> userResponse = users.stream()
                .map(UserResponse.Info::fromEntity)
                .collect(Collectors.toList());

        return new PagingResponse<>(page, userResponse, size, users.getTotalElements(), users.getTotalPages());

    }

    public MemberPointResponse.Info addPointToUser(User user,int depositPoint,Long userId){
        user.isAdmin();

        User existUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 멤버입니다."));

        existUser.addPoint(depositPoint);

        userRepository.save(existUser);

        return MemberPointResponse.Info.fromEntity(existUser);

    }

}
