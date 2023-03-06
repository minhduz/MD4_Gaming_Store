package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUserName(String username);
    boolean existsByUserName(String username);
    boolean existsByUserEmail(String userEmail);
    User findByUserEmail(String email);
}
