package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.Image;
import ra.model.repository.ImageRepository;
import ra.model.service.ImageService;

import java.util.List;
@Service
public class ImageServiceImp implements ImageService<Image,Integer> {
    @Autowired
    ImageRepository imageRepository;

    @Override
    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    @Override
    public Image findByID(Integer id) {
        return imageRepository.findById(id).get();
    }

    @Override
    public void saveOrUpdate(Image entity) {
        imageRepository.save(entity);
    }

    @Override
    public void deleteByID(Integer id) {
        imageRepository.deleteById(id);
    }

    @Override
    public List<Image> findByGame_GameID(int gameID) {
        return imageRepository.findByGame_GameID(gameID);
    }
}
