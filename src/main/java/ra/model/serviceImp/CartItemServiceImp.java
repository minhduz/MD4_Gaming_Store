package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.CartItem;
import ra.model.repository.CartItemRepository;
import ra.model.service.CartItemService;

import java.util.List;

@Service
public class CartItemServiceImp implements CartItemService<CartItem,Integer> {
    @Autowired
    CartItemRepository  cartItemRepository;

    @Override
    public List<CartItem> findAll() {
        return cartItemRepository.findAll();
    }

    @Override
    public CartItem findByID(Integer id) {
        return cartItemRepository.findById(id).get();
    }

    @Override
    public void saveOrUpdate(CartItem entity) {
        cartItemRepository.save(entity);
    }

    @Override
    public void deleteByID(Integer id) {
        System.out.println(id);
        try {
            cartItemRepository.deleteById(id);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteByEntity(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void deleteCart123(int cartItemID) {
        cartItemRepository.deleteCart123(cartItemID);
    }
}
