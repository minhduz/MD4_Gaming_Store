package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ra.model.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Integer>{
    List<Category> findByParentID(int parentID);

    List<Category> findByCategoryNameContaining(String categoryName);

    @Query(value = "from Category c where c.parentID = 0")
    List<Category> findParentCategory();
}
