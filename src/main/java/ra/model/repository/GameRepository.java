package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.model.entity.Category;
import ra.model.entity.Game;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface GameRepository extends JpaRepository<Game,Integer> {
    List<Game> findByGameNameContaining(String gameName);

    @Query(value = "insert into following (userID,gameID) values (:userId,:gameId)",nativeQuery = true)
    @Transactional
    @Modifying
    void insertIntoFollowing( @Param("userId") int userId,@Param("gameId") int gameId);
}
