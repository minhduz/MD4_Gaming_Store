package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.Orders;

import java.util.List;


public interface OrdersRepository extends JpaRepository<Orders,Integer> {
    List<Orders> findAllByUser_UserID(Integer userID);
}
