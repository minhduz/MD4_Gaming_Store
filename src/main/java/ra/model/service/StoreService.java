package ra.model.service;

import java.util.List;

public interface StoreService<T,V> {
    List<T> findAll();
    T findByID(V id);
    void saveOrUpdate(T entity);
    void deleteByID(V id);
}
