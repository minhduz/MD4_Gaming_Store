package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.Platform;

@Repository
public interface PlatformRepository extends JpaRepository<Platform,Integer> {

}
