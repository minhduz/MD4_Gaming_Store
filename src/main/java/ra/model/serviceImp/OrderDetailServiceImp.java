package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.OrderDetail;
import ra.model.repository.OrderDetailRepository;
import ra.model.service.OrderDetailService;

import java.util.List;
@Service
public class OrderDetailServiceImp implements OrderDetailService<OrderDetail,Integer> {
    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    @Override
    public OrderDetail findByID(Integer id) {
        return orderDetailRepository.findById(id).get();
    }

    @Override
    public void saveOrUpdate(OrderDetail entity) {
        orderDetailRepository.save(entity);
    }

    @Override
    public void deleteByID(Integer id) {
        orderDetailRepository.deleteById(id);
    }
}
