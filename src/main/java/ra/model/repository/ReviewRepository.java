package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.Review;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
}
