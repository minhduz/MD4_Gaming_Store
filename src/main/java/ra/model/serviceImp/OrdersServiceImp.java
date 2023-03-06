package ra.model.serviceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.Orders;

import ra.model.repository.OrdersRepository;
import ra.model.service.OrdersService;

import java.util.List;
@Service
public class OrdersServiceImp implements OrdersService<Orders,Integer> {
    @Autowired
    OrdersRepository ordersRepository;

    @Override
    public List<Orders> findAll() {
        return ordersRepository.findAll();
    }

    @Override
    public Orders findByID(Integer id) {
        return ordersRepository.findById(id).get();
    }

    @Override
    public void saveOrUpdate(Orders entity) {
        ordersRepository.save(entity);
    }

    @Override
    public void deleteByID(Integer id) {
        ordersRepository.deleteById(id);
    }

    @Override
    public List<Orders> findAllByUser_UserID(Integer userID) {
        return ordersRepository.findAllByUser_UserID(userID);
    }
}
