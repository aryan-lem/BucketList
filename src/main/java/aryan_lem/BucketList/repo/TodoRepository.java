package aryan_lem.BucketList.repo;

import aryan_lem.BucketList.model.Todo;
import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    Todo save(Todo todo);
    Optional<Todo> findById(String id);
    List<Todo> findByUserId(String userId);
    void delete(String id,String userId);

    void deleteById(String id);
}
