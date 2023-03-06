package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.Category;
import ra.model.entity.Game;
import ra.model.entity.Image;
import ra.model.entity.Review;
import ra.model.service.GameService;
import ra.model.service.ImageService;
import ra.payload.response.GameDTO;
import ra.payload.response.GameDTO2;
import ra.security.CustomUserDetails;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/game")
public class GameController {
    @Autowired
    GameService<Game, Integer> gameService;
    @Autowired
    ImageService<Image, Integer> imageService;

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("getAll")
    public List<GameDTO> getAll() {
        List<Game> listGame = gameService.findAll();
        List<GameDTO> listGameDTO = new ArrayList<>();
        for (Game game : listGame) {
            GameDTO gameDTO = new GameDTO();
            gameDTO.setGameID(game.getGameID());
            gameDTO.setGameName(game.getGameName());
            gameDTO.setGamePrice(game.getGamePrice());
            gameDTO.setGameMainImage(game.getGameMainImage());
            gameDTO.setGameStatus(game.isGameStatus());
            listGameDTO.add(gameDTO);
        }
        return listGameDTO;
    }

    @PermitAll
    @GetMapping("getHomeGame")
    public List<GameDTO2> getHomeGame() {
        List<Game> listGame = gameService.findAll();
        List<GameDTO2> listGameDTO2 = new ArrayList<>();
        for (Game game: listGame) {
            GameDTO2 gameDTO2 = new GameDTO2();
            if(game.isGameStatus()) {
                gameDTO2.setGameID(game.getGameID());
                gameDTO2.setGameName(game.getGameName());
                gameDTO2.setGameMainImage(game.getGameMainImage());
                gameDTO2.setGamePrice(game.getGamePrice());
                listGameDTO2.add(gameDTO2);
            }
        }
        return listGameDTO2;
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("getPagingGame")
    public ResponseEntity<Map<String, Object>> getPagingAndSortByName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam String direction) {
        Sort.Order order;
        if (direction.equals("asc")) {
            order = new Sort.Order(Sort.Direction.ASC, "gameID");
        } else {
            order = new Sort.Order(Sort.Direction.DESC, "gameID");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        Page<Game> pageBook = gameService.getPagination(pageable);
        Map<String, Object> data = new HashMap<>();
        List<Game> listGame = pageBook.getContent();
        List<GameDTO> listGameDTO = new ArrayList<>();
        for (Game game : listGame) {
            GameDTO gameDTO = new GameDTO();
            gameDTO.setGameID(game.getGameID());
            gameDTO.setGameName(game.getGameName());
            gameDTO.setGamePrice(game.getGamePrice());
            gameDTO.setGameMainImage(game.getGameMainImage());
            gameDTO.setGameStatus(game.isGameStatus());
            listGameDTO.add(gameDTO);
        }
        data.put("games", listGameDTO);
        data.put("total", pageBook.getSize());
        data.put("totalItems", pageBook.getTotalElements());
        data.put("totalPages", pageBook.getTotalPages());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping("create")
    public Game createGame(@RequestBody Game game) {
        gameService.saveOrUpdate(game);
        for (Image image : game.getListImage()) {
            Image subImage = new Image();
            subImage.setImageUrl(image.getImageUrl());
            subImage.setGame(game);
            imageService.saveOrUpdate(subImage);
        }
        return game;
    }

    @GetMapping("getByID/{gameID}")
    public Game getCategoryByID(@PathVariable("gameID") int gameID) {
        Game game = gameService.findByID(gameID);
        List<Review> listCheckedReview = checkReview(game.getListReview());
        game.setListReview(listCheckedReview);
        return game;
    }

    public List<Review> checkReview(List<Review> listReview) {
        List<Review> listCheckedReview = new ArrayList<>();
        for (Review re : listReview) {
            if (re.isReviewStatus()) {
                listCheckedReview.add(re);
            }
        }
        return listCheckedReview;
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @PutMapping("update/{gameID}")
    public Game updateGame(@PathVariable("gameID") int gameID, @RequestBody Game game) {
        Game gameUpdate = gameService.findByID(gameID);
        List<Image> listSubImage = imageService.findByGame_GameID(gameID);
        for (Image image: listSubImage) {
            imageService.deleteByID(image.getImageID());
        }
        gameUpdate.setGameName(game.getGameName() != null ? game.getGameName() : gameUpdate.getGameName());
        gameUpdate.setGamePrice(game.getGamePrice());
        gameUpdate.setGameDescription(game.getGameDescription());
        gameUpdate.setGameDeveloper(game.getGameDeveloper());
        gameUpdate.setGamePublisher(game.getGamePublisher());
        gameUpdate.setGameMainImage(game.getGameMainImage());
        gameUpdate.setGameReleaseDate(game.getGameReleaseDate());
        gameUpdate.setGameDiscount(game.getGameDiscount());
        gameUpdate.setGameStatus(game.isGameStatus());
        gameUpdate.setListCategory(game.getListCategory());
        gameUpdate.setListPlatform(game.getListPlatform());
        for (Image image : game.getListImage()) {
            Image subImage = new Image();
            subImage.setImageUrl(image.getImageUrl());
            subImage.setGame(gameUpdate);
            imageService.saveOrUpdate(subImage);
        }
        gameService.saveOrUpdate(gameUpdate);
        return gameUpdate;
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping("addImages")
    public List<Image> updateImage(@RequestBody List<Image> listImage) {
        ;
        for (Image image : listImage) {
            imageService.saveOrUpdate(image);
        }
        return listImage;
    }

    @GetMapping("getGameByName/{gameName}")
    public List<Game> getGameByName(@PathVariable("gameName") String gameName) {
        return gameService.findByGameNameContaining(gameName);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("following/{gameID}")
    public ResponseEntity<?> addFollowing(@PathVariable("gameID") int gameID) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        gameService.insertIntoFollowing(userDetails.getUserID(), gameID);
        return ResponseEntity.ok("Game added into following successfully");
    }

}
