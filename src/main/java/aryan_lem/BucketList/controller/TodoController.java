package aryan_lem.BucketList.controller;

import aryan_lem.BucketList.model.Todo;
import aryan_lem.BucketList.service.TodoService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService todoService;
    public TodoController(TodoService todoService) { this.todoService = todoService; }

    @GetMapping
    public List<Todo> getTodos(Authentication auth) {
        return todoService.getTodosByUser(auth.getName());
    }

    @PostMapping
    public Todo add(@RequestBody Todo todo, Authentication auth) {
        todo.setUserId(auth.getName());
        return todoService.create(todo);
    }

    @PutMapping("/{id}")
    public Todo update(@PathVariable String id, @RequestBody Todo todo) {
        return todoService.update(id, todo).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        todoService.delete(id);
    }
}

