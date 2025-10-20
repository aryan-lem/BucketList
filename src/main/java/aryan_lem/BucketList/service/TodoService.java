package aryan_lem.BucketList.service;

import aryan_lem.BucketList.model.Todo;
import aryan_lem.BucketList.repo.TodoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    private final TodoRepository todoRepo;

    public TodoService(TodoRepository todoRepo) {
        this.todoRepo = todoRepo;
    }

    public List<Todo> getTodosByUser(String userId) {
        return todoRepo.findByUserId(userId);
    }

    public Todo create(Todo todo) {
        return todoRepo.save(todo);
    }

    public Optional<Todo> update(String id, Todo updatedTodo) {
        return todoRepo.findById(id).map(t -> {
            t.setTitle(updatedTodo.getTitle());
            t.setCompleted(updatedTodo.isCompleted());
            return todoRepo.save(t);
        });
    }

    public void delete(String id) {
        todoRepo.deleteById(id);
    }
}
