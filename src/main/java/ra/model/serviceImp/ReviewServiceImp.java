package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.Review;
import ra.model.repository.ReviewRepository;
import ra.model.service.ReviewService;

import java.util.List;

@Service
public class ReviewServiceImp implements ReviewService<Review,Integer> {
    @Autowired
    ReviewRepository reviewRepository;

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public Review findByID(Integer id) {
        return reviewRepository.findById(id).get();
    }

    @Override
    public void saveOrUpdate(Review entity) {
        reviewRepository.save(entity);
    }

    @Override
    public void deleteByID(Integer id) {
        reviewRepository.deleteById(id);
    }
}
