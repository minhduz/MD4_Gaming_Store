package ra.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.repository.query.Param;
import ra.model.entity.Category;
import ra.model.entity.Game;

import java.util.List;

public interface GameService<T,V> extends StoreService<T,V>  {
    List<Game> findByGameNameContaining(String gameName);
    Page<Game> getPagination(Pageable pageable);
    void insertIntoFollowing(int userID,int gameID );

}
