package gift.service.auth;

import gift.domain.auth.User;
import gift.domain.auth.User.CreateUser;
import gift.domain.auth.User.UpdateUser;
import gift.domain.auth.User.UserSimple;
import gift.entity.auth.UserEntity;
import gift.mapper.auth.UserMapper;
import gift.repository.auth.UserRepository;
import gift.util.errorException.BaseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Page<UserSimple> getUserList(User.getList param) {
        return userMapper.toSimpleList(userRepository.findAllByIsDelete(0, param.toPageable()));
    }

    public UserEntity getUser(long id) {
        return userRepository.findByIdAndIsDelete(id, 0)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));
    }

    public Long createUser(CreateUser create) {
        if (userRepository.findByEmailAndIsDelete(create.getEmail(), 0).isPresent()) {
            throw new BaseHandler(HttpStatus.FORBIDDEN, "중복된 유저가 존재합니다.");
        }

        UserEntity userEntity = userMapper.toEntity(create);
        userRepository.save(userEntity);
        return userEntity.getId();
    }

    @Transactional
    public Long updatePassword(long id, UpdateUser update) {
        UserEntity userEntity = userRepository.findByIdAndIsDelete(id, 0)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        userMapper.toUpdate(update, userEntity);
//        userRepository.save(userMapper.toUpdate(update, userEntity));
        return userEntity.getId();
    }

    public Long deleteUser(long id) {
        UserEntity userEntity = userRepository.findByIdAndIsDelete(id, 0)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        userRepository.save(userMapper.toDelete(userEntity));
        return userEntity.getId();
    }
}
