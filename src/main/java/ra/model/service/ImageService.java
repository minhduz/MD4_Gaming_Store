package ra.model.service;

import ra.model.entity.Image;

import java.util.List;

public interface ImageService<T,V> extends StoreService<T,V> {
    List<Image> findByGame_GameID(int gameID);
}
