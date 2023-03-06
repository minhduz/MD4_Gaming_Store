package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating,Integer> {
}
