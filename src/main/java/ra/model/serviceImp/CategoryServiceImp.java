package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.entity.Category;
import ra.model.repository.CategoryRepository;
import ra.model.service.CategoryService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService<Category,Integer> {
    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findByID(Integer id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public void saveOrUpdate(Category entity) {
        categoryRepository.save(entity);
    }

    @Override
    public void deleteByID(Integer id) {
        categoryRepository.deleteById(id);
    }
    @Override
    public List<Category> findByParentID(int parentID){
        return categoryRepository.findByParentID(parentID);
    };
    @Override
    public List<Category> findParentCategory(){return  categoryRepository.findParentCategory();}

    @Override
    public List<Category> findByCategoryNameContaining(String categoryName) {
        return categoryRepository.findByCategoryNameContaining(categoryName);
    }
    @Override
    public Page<Category> getPagination(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

}
