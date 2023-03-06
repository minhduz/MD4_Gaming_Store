package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.Rating;
import ra.model.repository.RatingRepository;
import ra.model.service.RatingService;

import java.util.List;
@Service
public class RatingServiceImp implements RatingService<Rating,Integer> {
    @Autowired
    RatingRepository ratingRepository;

    @Override
    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    @Override
    public Rating findByID(Integer id) {
        return ratingRepository.findById(id).get();
    }

    @Override
    public void saveOrUpdate(Rating entity) {
        ratingRepository.save(entity);
    }

    @Override
    public void deleteByID(Integer id) {
        ratingRepository.deleteById(id);
    }
}
