package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.Platform;
import ra.model.repository.PlatformRepository;
import ra.model.service.PlatformService;

import java.util.List;
@Service
public class PlatformServiceImp implements PlatformService<Platform,Integer> {
    @Autowired
    PlatformRepository platformRepository;
    @Override
    public List<Platform> findAll() {
        return platformRepository.findAll();
    }

    @Override
    public Platform findByID(Integer id) {
        return platformRepository.findById(id).get();
    }

    @Override
    public void saveOrUpdate(Platform entity) {
        platformRepository.save(entity);
    }

    @Override
    public void deleteByID(Integer id) {
        platformRepository.deleteById(id);
    }
}
