package ra.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.entity.Category;

import java.util.List;

public interface CategoryService<T,V> extends StoreService<T,V> {
    List<Category> findByParentID(int parentID);
    List<Category> findParentCategory();
    List<Category> findByCategoryNameContaining(String categoryName);

    Page<Category> getPagination(Pageable pageable);

}
