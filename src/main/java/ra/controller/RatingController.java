package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.*;
import ra.model.service.GameService;
import ra.model.service.RatingService;
import ra.model.service.ReviewService;
import ra.model.service.UserService;
import ra.payload.response.GameDTO;
import ra.payload.response.RatingDTO;
import ra.payload.response.UserDTO2;
import ra.security.CustomUserDetails;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("api/v1/rating")
public class RatingController {
    @Autowired
    RatingService<Rating,Integer> ratingService;

    @Autowired
    UserService<User,Integer> userService;

    @Autowired
    GameService<Game,Integer> gameService;

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("getAll")
    public List<RatingDTO> getAll(){
        List<Rating> listRating = ratingService.findAll();
        List<RatingDTO> listRatingDTO = new ArrayList<>();
        for (Rating rt:listRating) {
            RatingDTO ratingDTO = new RatingDTO();
            ratingDTO.setRatingID(rt.getRatingID());
            ratingDTO.setRatingValue(rt.getRatingValue());
            GameDTO gameDTO = new GameDTO(rt.getGame().getGameID(),rt.getGame().getGameName(),rt.getGame().getGamePrice(),rt.getGame().getGameMainImage(),rt.getGame().isGameStatus());
            ratingDTO.setGameDTO(gameDTO);
            UserDTO2 userDTO2 = new UserDTO2();
            userDTO2.setUserID(rt.getUser().getUserID());
            userDTO2.setUserName(rt.getUser().getUserName());
            userDTO2.setUserEmail(rt.getUser().getUserEmail());
            ratingDTO.setUserDTO2(userDTO2);
            listRatingDTO.add(ratingDTO);
        }
        return listRatingDTO;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("create/{gameID}")
    public ResponseEntity<?> addReview(@PathVariable("gameID")int gameID, @RequestBody Rating rating){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByID(userDetails.getUserID());
        if(checkBought(user,gameID)){
            rating.setUser(user);
            rating.setGame(gameService.findByID(gameID));
            ratingService.saveOrUpdate(rating);
            return ResponseEntity.ok("Thank for rating");
        }else {
            return ResponseEntity.ok("You have to buy this game to rate it");
        }
    }

    public boolean checkBought(User user,int gameID){
        for (Orders od:user.getListOrders()) {
            for (OrderDetail ord :od.getListOrderDetail()) {
                if(ord.getGame().getGameID()==gameID&&od.isOrderStatus()) {
                    return true;
                }
            }
        }
        return false;
    }
}
