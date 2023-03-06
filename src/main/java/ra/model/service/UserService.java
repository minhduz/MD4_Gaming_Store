package ra.model.service;

import org.springframework.stereotype.Repository;
import ra.model.entity.User;

@Repository
public interface UserService<T,V> extends StoreService<T,V> {
    User findByUserName(String userName);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    User findByUserEmail(String email);
}
