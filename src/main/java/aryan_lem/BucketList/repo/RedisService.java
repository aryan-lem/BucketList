package aryan_lem.BucketList.repo;

import aryan_lem.BucketList.model.User;
import aryan_lem.BucketList.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RedisService implements UserRepository, TodoRepository {

    private final RedisTemplate<String,Object> redisTemplate;
    private final ValueOperations<String,Object> vops;
    private final SetOperations<String,Object> sops;

    @Autowired
    public RedisService(RedisTemplate<String,Object> redisTemplate){
        this.redisTemplate=redisTemplate;
        this.vops=redisTemplate.opsForValue();
        this.sops=redisTemplate.opsForSet();
    }

    // User methods
    @Override
    public void save(User user){
        vops.set(userKey(user.getUsername()),user);
    }

    @Override
    public Optional<User> findByUsername(String username){
        Object o=vops.get(userKey(username));
        return o==null?Optional.empty():Optional.of((User)o);
    }

    // Todo methods
    @Override
    public Todo save(Todo td){
        vops.set(todoKey(td.getId()),td);
        sops.add(todosSetKey(td.getUserId()),td.getId());
        return td;
    }

    @Override
    public Optional<Todo> findById(String id){
        Object o=vops.get(todoKey(id));
        return o==null?Optional.empty():Optional.of((Todo)o);
    }

    @Override
    public List<Todo> findByUserId(String userId){
        Set<Object> ids=sops.members(todosSetKey(userId));
        if(ids==null||ids.isEmpty())return Collections.emptyList();
        return ids.stream()
                .map(x->(String)x)
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id,String userId){
        redisTemplate.delete(todoKey(id));
        sops.remove(todosSetKey(userId),id);
    }

    @Override
    public void deleteById(String id) {
        Optional<Todo> todoOpt = findById(id);
        if (todoOpt.isPresent()) {
            Todo td = todoOpt.get();
            redisTemplate.delete(todoKey(id));
            sops.remove(todosSetKey(td.getUserId()), id);
        }
    }

    // Token operations
    public void saveTokens(String username,Map<String,Object> tokens){
        vops.set(tokenKey(username),tokens);
    }

    @SuppressWarnings("unchecked")
    public Optional<Map<String,Object>> getTokens(String username){
        Object o=vops.get(tokenKey(username));
        return o==null?Optional.empty():Optional.of((Map<String,Object>)o);
    }

    public void deleteTokens(String username){
        redisTemplate.delete(tokenKey(username));
    }

    // Key helpers
    private String userKey(String username){return"user:"+username;}
    private String todoKey(String id){return"todo:"+id;}
    private String todosSetKey(String userId){return"todos:"+userId;}
    private String tokenKey(String username){return"token:"+username;}
}
