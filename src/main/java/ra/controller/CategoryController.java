package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.Category;
import ra.model.entity.Game;
import ra.model.service.CategoryService;
import ra.payload.response.CategoryDTO;
import ra.payload.response.CategoryDTO2;
import ra.payload.response.CategoryDTO3;
import ra.payload.response.GameDTO;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/category")
public class CategoryController  {
    @Autowired
    CategoryService<Category,Integer> categoryService;
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("getAll")
    public List<Category> getAll(){
        return categoryService.findAll();
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("getPagingAndSortByID")
    public ResponseEntity<Map<String,Object>> getPagingAndSortByName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String direction) {
        Sort.Order order;
        if (direction.equals("asc")){
            order=new Sort.Order(Sort.Direction.ASC,"categoryID");
        }else{
            order=new Sort.Order(Sort.Direction.DESC,"categoryID");
        }
        Pageable pageable = PageRequest.of(page,size,Sort.by(order));
        Page<Category> pageCategory = categoryService.getPagination(pageable);
        Map<String,Object> data = new HashMap<>();
        List<Category> listCategory = pageCategory.getContent();
        List<CategoryDTO3> listCategoryDTO3 = new ArrayList<>();
        for (Category cat: listCategory) {
            CategoryDTO3 categoryDTO3 = new CategoryDTO3();
            categoryDTO3.setCatID(cat.getCategoryID());
            categoryDTO3.setCatName(cat.getCategoryName());
            categoryDTO3.setCatStatus(cat.isCategoryStatus());
            categoryDTO3.setParentID(cat.getParentID());
            if(cat.getParentID() == 0){
                categoryDTO3.setCatParentName(null);
            }else {
                Category catParent = categoryService.findByID(cat.getParentID());
                categoryDTO3.setCatParentName(catParent.getCategoryName());
            }
            listCategoryDTO3.add(categoryDTO3);
        }
        data.put("category",listCategoryDTO3);
        data.put("total",pageCategory.getSize());
        data.put("totalItems",pageCategory.getTotalElements());
        data.put("totalPages",pageCategory.getTotalPages());
        return  new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping("getHomeCategory")
    public List<CategoryDTO> getAllHomeCategory(){
        List<CategoryDTO> listCatDTO = new ArrayList<>();
        List<Category> listCategory = categoryService.findParentCategory();
        for (Category cat:listCategory) {
            if(cat.isCategoryStatus()) {
                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setCatID(cat.getCategoryID());
                categoryDTO.setCatName(cat.getCategoryName());
                categoryDTO.setListCatChild(getChildCategory(categoryDTO));
                listCatDTO.add(categoryDTO);
            }
        }
        return listCatDTO;
    }

    public List<CategoryDTO> getChildCategory(CategoryDTO parent){
        List<CategoryDTO> listChildCategory = new ArrayList<>();
        List<Category> listChild = categoryService.findByParentID(parent.getCatID());
        List<CategoryDTO> listChildDTO = new ArrayList<>();
        for (Category catChild:listChild) {
            if(catChild.isCategoryStatus()) {
                CategoryDTO categoryChildDTO = new CategoryDTO();
                categoryChildDTO.setCatID(catChild.getCategoryID());
                categoryChildDTO.setCatName(catChild.getCategoryName());
                categoryChildDTO.setListCatChild(getChildCategory(categoryChildDTO));
                listChildDTO.add(categoryChildDTO);
            }
        }
        for (CategoryDTO child : listChildDTO) {
            listChildCategory.add(child);
            listChildCategory.addAll(getChildCategory(child));
        }
        return listChildCategory;
    }




    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("findByID/{categoryID}")
    public Category getCategoryByID(@PathVariable("categoryID") int categoryID){
        return categoryService.findByID(categoryID);
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("findByName/{categoryName}")
    public List<CategoryDTO3> getCategoryByName(@PathVariable("categoryName") String categoryName){
        List<Category> listCategory = categoryService.findByCategoryNameContaining(categoryName);
        List<CategoryDTO3> listCategoryDTO3 = new ArrayList<>();
        for (Category cat: listCategory) {
            CategoryDTO3 categoryDTO3 = new CategoryDTO3();
            categoryDTO3.setCatID(cat.getCategoryID());
            categoryDTO3.setCatName(cat.getCategoryName());
            categoryDTO3.setCatStatus(cat.isCategoryStatus());
            categoryDTO3.setParentID(cat.getParentID());
            if(cat.getParentID() == 0){
                categoryDTO3.setCatParentName(null);
            }else {
                Category catParent = categoryService.findByID(cat.getParentID());
                categoryDTO3.setCatParentName(catParent.getCategoryName());
            }
            listCategoryDTO3.add(categoryDTO3);
        }
        return listCategoryDTO3;
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public void createCategory(@RequestBody Category category){
        categoryService.saveOrUpdate(category);
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @DeleteMapping ("/delete/{categoryID}")
    public void deleteCategory(@PathVariable("categoryID") int categoryID){
        categoryService.deleteByID(categoryID);
    }


    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{categoryID}")
    public void updateCategory(@PathVariable("categoryID") int categoryID,@RequestBody Category category){
        Category categoryUpdate = categoryService.findByID(categoryID);
        categoryUpdate.setCategoryName(category.getCategoryName());
        categoryUpdate.setCategoryStatus(category.isCategoryStatus());
        categoryUpdate.setParentID(category.getParentID());
        categoryService.saveOrUpdate(categoryUpdate);
    }


    @GetMapping("findCategoryGame/{categoryID}")
    public CategoryDTO2 getCategoryGame(@PathVariable("categoryID")int categoryID){
        Category category = categoryService.findByID(categoryID);
        CategoryDTO2 categoryDTO2 = new CategoryDTO2();
        categoryDTO2.setCategoryID(category.getCategoryID());
        categoryDTO2.setCategoryName(category.getCategoryName());
        categoryDTO2.setCategoryStatus(category.isCategoryStatus());
        List<GameDTO> listGameDTO = new ArrayList<>();
        for (Game game:category.getListGame()) {
            if(game.isGameStatus()) {
                GameDTO gameDTO = new GameDTO(game.getGameID(), game.getGameName(), game.getGamePrice(), game.getGameMainImage(), game.isGameStatus());
                listGameDTO.add(gameDTO);
            }
        }
        categoryDTO2.setListGameDTO(listGameDTO);
        return categoryDTO2;

    }
}
