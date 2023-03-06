package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer> {
}
