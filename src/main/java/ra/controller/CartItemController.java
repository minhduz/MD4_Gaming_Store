package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.CartItem;
import ra.model.entity.Game;
import ra.model.entity.User;
import ra.model.service.CartItemService;
import ra.model.service.GameService;
import ra.model.service.UserService;
import ra.payload.response.CartDTO;
import ra.security.CustomUserDetails;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("api/v1/cart")
public class CartItemController {
    @Autowired
    CartItemService<CartItem,Integer> cartItemService;
    @Autowired
    GameService<Game,Integer> gameService;
    @Autowired
    UserService<User,Integer> userService;

    public User getUser(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByID(userDetails.getUserID());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("getAll")
    public List<CartDTO> getAll(){

        List<CartDTO> listCartDTO = new ArrayList<>();
        for (CartItem cartItem: getUser().getListCartItem()) {
            CartDTO cartDTO = new CartDTO();
            cartDTO.setCartID(cartItem.getCartItemID());
            cartDTO.setGameMainImage(cartItem.getGame().getGameMainImage());
            cartDTO.setGameName(cartItem.getGame().getGameName());
            cartDTO.setCartQuantity(cartItem.getCartQuantity());
            float price = cartItem.getCartQuantity()*(cartItem.getGame().getGamePrice()*(100-cartItem.getGame().getGameDiscount())/100);
            cartDTO.setCartPrice(price);
            listCartDTO.add(cartDTO);
        }
        return listCartDTO;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("getAll/{cartID}")
    public CartItem findByID(@PathVariable("cartID")int cartID){
        return cartItemService.findByID(cartID);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("addCart/{gameID}")
    public ResponseEntity<?> addCart(@PathVariable("gameID")int gameID, @RequestBody CartItem cartItem){
        Game game = gameService.findByID(gameID);
        cartItem.setCartPrice(cartItem.getCartQuantity()*(game.getGamePrice()*(100-game.getGameDiscount())/100));
        cartItem.setGame(game);
        cartItem.setUser(getUser());
        cartItemService.saveOrUpdate(cartItem);
        return ResponseEntity.ok("Game has been added to cart, total items in carts "+getUser().getListCartItem().size());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("removeCart/{cartID}")
    public ResponseEntity<?> removeCart(@PathVariable("cartID")int cartID){
//        cartItemService.deleteCart123(cartID);
cartItemService.deleteByID(cartID);
        return ResponseEntity.ok("Game has been removed from cart, total items in cart "+getUser().getListCartItem().size());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("updateCart/{cartID}")
    public List<CartDTO> updateCart(@PathVariable("cartID")int cartID,@RequestBody CartItem cartItem){
        CartItem cartItemUpdate = cartItemService.findByID(cartID);
        float price = cartItem.getCartQuantity()*(cartItemUpdate.getGame().getGamePrice()*(100-cartItemUpdate.getGame().getGameDiscount())/100);
        cartItemUpdate.setCartQuantity(cartItem.getCartQuantity());
        cartItemUpdate.setCartPrice(price);
        cartItemService.saveOrUpdate(cartItemUpdate);
        return getAll();
    }
}
