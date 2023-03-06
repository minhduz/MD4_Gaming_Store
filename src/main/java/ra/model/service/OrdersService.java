package ra.model.service;

import ra.model.entity.Orders;

import java.util.List;

public interface OrdersService<T,V> extends StoreService<T,V>  {
    List<Orders> findAllByUser_UserID(Integer userID);
}
