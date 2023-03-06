package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.entity.Category;
import ra.model.entity.Game;
import ra.model.repository.GameRepository;
import ra.model.service.GameService;

import java.util.List;

@Service
public class GameServiceImp implements GameService<Game,Integer> {
    @Autowired
    GameRepository gameRepository;

    @Override
    public List<Game> findByGameNameContaining(String gameName) {
        return gameRepository.findByGameNameContaining(gameName);
    }

    @Override
    public Page<Game> getPagination(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    @Override
    public void insertIntoFollowing(int userID, int gameID) {
        gameRepository.insertIntoFollowing(userID,gameID);
    }


    @Override
    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    @Override
    public Game findByID(Integer id) {
        return gameRepository.findById(id).get();
    }

    @Override
    public void saveOrUpdate(Game entity) {
        gameRepository.save(entity);
    }

    @Override
    public void deleteByID(Integer id) {
        gameRepository.deleteById(id);
    }
}
