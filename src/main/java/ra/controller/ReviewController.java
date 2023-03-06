package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.*;
import ra.model.service.GameService;
import ra.model.service.ReviewService;
import ra.model.service.UserService;
import ra.payload.response.GameDTO;
import ra.payload.response.ReviewDTO;
import ra.payload.response.UserDTO2;
import ra.security.CustomUserDetails;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("api/v1/review")
public class ReviewController {
    @Autowired
    ReviewService<Review,Integer> reviewService;

    @Autowired
    UserService<User,Integer> userService;

    @Autowired
    GameService<Game,Integer> gameService;

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("getAll")
    public List<ReviewDTO> getAll(){
        List<Review> listReview = reviewService.findAll();
        List<ReviewDTO> listReviewDTO = new ArrayList<>();
        for (Review re:listReview) {
            ReviewDTO reviewDTO = new ReviewDTO();
            reviewDTO.setReviewID(re.getReviewID());
            reviewDTO.setReviewContent(re.getReviewContent());
            reviewDTO.setReviewStatus(re.isReviewStatus());
            GameDTO gameDTO = new GameDTO(re.getGame().getGameID(),re.getGame().getGameName(),re.getGame().getGamePrice(),re.getGame().getGameMainImage(),re.getGame().isGameStatus());
            reviewDTO.setGameDTO(gameDTO);
            UserDTO2 userDTO2 = new UserDTO2();
            userDTO2.setUserID(re.getUser().getUserID());
            userDTO2.setUserName(re.getUser().getUserName());
            userDTO2.setUserEmail(re.getUser().getUserEmail());
            reviewDTO.setUserDTO2(userDTO2);
            listReviewDTO.add(reviewDTO);
        }
        return listReviewDTO;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("create/{gameID}")
    public ResponseEntity<?> addReview(@PathVariable("gameID")int gameID, @RequestBody Review review){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByID(userDetails.getUserID());
        if(checkBought(user,gameID)){
            review.setUser(user);
            review.setGame(gameService.findByID(gameID));
            review.setReviewStatus(false);
            reviewService.saveOrUpdate(review);
            return ResponseEntity.ok("Your review have been summited");
        }else {
            return ResponseEntity.ok("You have to buy this game to review it");
        }
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping ("updateStatus/{reviewID}")
    public Review updateStatus(@PathVariable("reviewID")int reviewID,@RequestBody Review review){
        Review reviewUpdate = reviewService.findByID(reviewID);
        reviewUpdate.setReviewStatus(review.isReviewStatus());
        reviewService.saveOrUpdate(reviewUpdate);
        return reviewUpdate;
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
















