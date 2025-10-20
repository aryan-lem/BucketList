package aryan_lem.BucketList.repo;

import aryan_lem.BucketList.model.User;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findByUsername(String username);
}
