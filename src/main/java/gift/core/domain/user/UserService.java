package gift.core.domain.user;

public interface UserService {

    void registerUser(User user);

    void deleteUserById(Long id);

    User getUserById(Long id);

}
