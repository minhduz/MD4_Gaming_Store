package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.model.entity.CartItem;

import javax.transaction.Transactional;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Integer>{
    @Modifying
    @Transactional
    @Query("delete from CartItem c where c.cartItemID=:cartItemID")
    void deleteCart123(@Param("cartItemID") int cartItemID);


}
