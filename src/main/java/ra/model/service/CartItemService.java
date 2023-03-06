package ra.model.service;

import org.springframework.data.repository.query.Param;

public interface CartItemService<T,V> extends StoreService<T,V>  {
    void deleteByEntity(T t);
    void deleteCart123(int cartItemID);
}
